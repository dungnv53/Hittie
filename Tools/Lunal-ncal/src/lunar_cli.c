#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// Struct to store a Date
typedef struct {
    int day;
    int month;
    int year;
} Date;

// Lookup database from 2020 to 2030 (Vietnamese Lunar Calendar UTC+7)
// Each entry: {Solar Year, Lunar New Year Day, Lunar New Year Month, Leap Month (0 if none), Month lengths as bitmask (1=30 days, 0=29 days)}
typedef struct {
    int year;
    int lny_day;   // Solar Day of Lunar New Year
    int lny_month; // Solar Month of Lunar New Year
    int leap_mon;  // Leap month index (1-12, 0 if none)
    unsigned int months_map; // 12 or 13 bits indicating 30 (1) or 29 (0) days
} LunarYearInfo;

static const LunarYearInfo LUNAR_DB[] = {
    {2020, 25, 1, 4, 0x1A95}, // 01101010010101
    {2021, 12, 2, 0, 0x0D4A}, // 00110101001010
    {2022, 1,  2, 0, 0x0DA5}, // 00110110100101
    {2023, 22, 1, 2, 0x15AA}, // 01010110101010
    {2024, 10, 2, 0, 0x056A}, // 00010101101010
    {2025, 29, 1, 0, 0x0AAD}, // 00101010101101
    {2026, 17, 2, 6, 0x1556}, // 01010101010110
    {2027, 6,  2, 0, 0x0A5B}, // 00101001011011
    {2028, 26, 1, 5, 0x152B}, // 01010100101011
    {2029, 13, 2, 0, 0x0A2B}, // 00101000101011
    {2030, 3,  2, 0, 0x0953}  // 00100101010011
};

#define DB_SIZE (sizeof(LUNAR_DB)/sizeof(LUNAR_DB[0]))

// Can Chi Arrays
const char* CAN[] = {"Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý"};
const char* CHI[] = {"Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi"};

// Helper: Calculate Julian Day Number
long get_julian_day(int d, int m, int y) {
    if (m <= 2) {
        y -= 1;
        m += 12;
    }
    long A = y / 100;
    long B = A / 4;
    long C = 2 - A + B;
    long E = 365.25 * (y + 4716);
    long F = 30.6001 * (m + 1);
    return C + d + E + F - 1524.5;
}

// Convert Solar to Lunar using the lookup table
void solar_to_lunar(int sd, int sm, int sy, int *ld, int *lm, int *ly, int *is_leap) {
    *ld = 1; *lm = 1; *ly = sy; *is_leap = 0;
    
    // Find database entry
    const LunarYearInfo *info = NULL;
    for (size_t i = 0; i < DB_SIZE; i++) {
        if (LUNAR_DB[i].year == sy) {
            info = &LUNAR_DB[i];
            break;
        }
    }
    if (!info) return; // Out of range

    long jd_current = get_julian_day(sd, sm, sy);
    long jd_lny = get_julian_day(info->lny_day, info->lny_month, sy);

    // If current solar date is before this year's Lunar New Year
    if (jd_current < jd_lny) {
        // Find previous year's info
        const LunarYearInfo *prev_info = NULL;
        for (size_t i = 0; i < DB_SIZE; i++) {
            if (LUNAR_DB[i].year == sy - 1) {
                prev_info = &LUNAR_DB[i];
                break;
            }
        }
        if (!prev_info) return;
        
        info = prev_info;
        jd_lny = get_julian_day(info->lny_day, info->lny_month, sy - 1);
    }

    long days_diff = jd_current - jd_lny;
    *ly = info->year;

    // Iterate through months starting from Month 1 (Tháng Giêng)
    int current_month = 1;
    int has_leap = info->leap_mon > 0;
    int is_leap_month_active = 0;
    int total_months = has_leap ? 13 : 12;

    for (int i = 0; i < total_months; i++) {
        // Calculate days in the current loop month (using the bitmask)
        int month_days = ((info->months_map >> (total_months - 1 - i)) & 1) ? 30 : 29;

        if (days_diff < month_days) {
            *ld = (int)days_diff + 1;
            *lm = current_month;
            *is_leap = is_leap_month_active;
            return;
        }

        days_diff -= month_days;

        // Advance months taking leap month into account
        if (has_leap && current_month == info->leap_mon && !is_leap_month_active) {
            is_leap_month_active = 1; // Enter the leap month next loop
        } else {
            current_month++;
            is_leap_month_active = 0;
        }
    }
}

// Get the Year's Can Chi
void get_can_chi_year(int lunar_year, char* buffer) {
    int can_idx = (lunar_year + 6) % 10;
    int chi_idx = (lunar_year + 8) % 12;
    sprintf(buffer, "%s %s", CAN[can_idx], CHI[chi_idx]);
}

int main() {
    // 1. Get current Solar date
    time_t t = time(NULL);
    struct tm tm = *localtime(&t);
    int sd = tm.tm_mday;
    int sm = tm.tm_mon + 1;
    int sy = tm.tm_year + 1900;

    // 2. Convert to Lunar
    int ld, lm, ly, is_leap;
    solar_to_lunar(sd, sm, sy, &ld, &lm, &ly, &is_leap);

    // 3. Get Can Chi name
    char can_chi_year[50];
    get_can_chi_year(ly, can_chi_year);

    // 4. Output results
    printf("=========================================\n");
    printf("        LUNAR CLI DATE UTILITY           \n");
    printf("=========================================\n");
    printf(" Solar Date : %02d/%02d/%d\n", sd, sm, sy);
    printf("-----------------------------------------\n");
    printf(" Lunar Date : %02d/%02d/%d %s\n", ld, lm, ly, is_leap ? "(Nhuận)" : "");
    printf(" Year Name  : Năm %s\n", can_chi_year);
    printf("=========================================\n");

    return 0;
}
