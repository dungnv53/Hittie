#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct {
    int year;
    int lny_day;   
    int lny_month; 
    int leap_mon;  
    unsigned int months_map; 
} LunarYearInfo;

static const LunarYearInfo LUNAR_DB[] = {
    {2020, 25, 1, 4, 0x1A95}, {2021, 12, 2, 0, 0x0D4A},
    {2022, 1,  2, 0, 0x0DA5}, {2023, 22, 1, 2, 0x15AA},
    {2024, 10, 2, 0, 0x056A}, {2025, 29, 1, 0, 0x0AAD},
    {2026, 17, 2, 6, 0x1556}, {2027, 6,  2, 0, 0x0A5B},
    {2028, 26, 1, 5, 0x152B}, {2029, 13, 2, 0, 0x0A2B},
    {2030, 3,  2, 0, 0x0953}
};
#define DB_SIZE (sizeof(LUNAR_DB)/sizeof(LUNAR_DB[0]))

const char* CAN[] = {"Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý"};
const char* CHI[] = {"Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi"};

long get_julian_day(int d, int m, int y) {
    if (m <= 2) { y -= 1; m += 12; }
    long A = y / 100; long B = A / 4; long C = 2 - A + B;
    long E = 365.25 * (y + 4716); long F = 30.6001 * (m + 1);
    return C + d + E + F - 1524.5;
}

// Zeller's Congruence to find Weekday (0 = Sunday, 1 = Monday, ..., 6 = Saturday)
int get_weekday(int d, int m, int y) {
    if (m < 3) { m += 12; y -= 1; }
    int k = y % 100; int j = y / 100;
    int h = (d + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7;
    return (h + 5) % 7; // Convert to 0=Sun, 1=Mon...
}

void solar_to_lunar(int sd, int sm, int sy, int *ld, int *lm, int *ly, int *is_leap, int *m_days, long *m_start_jd) {
    const LunarYearInfo *info = NULL;
    for (size_t i = 0; i < DB_SIZE; i++) { if (LUNAR_DB[i].year == sy) { info = &LUNAR_DB[i]; break; } }
    if (!info) return;

    long jd_current = get_julian_day(sd, sm, sy);
    long jd_lny = get_julian_day(info->lny_day, info->lny_month, sy);

    if (jd_current < jd_lny) {
        const LunarYearInfo *prev_info = NULL;
        for (size_t i = 0; i < DB_SIZE; i++) { if (LUNAR_DB[i].year == sy - 1) { prev_info = &LUNAR_DB[i]; break; } }
        if (!prev_info) return;
        info = prev_info;
        jd_lny = get_julian_day(info->lny_day, info->lny_month, sy - 1);
    }

    long days_diff = jd_current - jd_lny;
    *ly = info->year;

    int current_month = 1;
    int has_leap = info->leap_mon > 0;
    int is_leap_month_active = 0;
    int total_months = has_leap ? 13 : 12;
    long accumulated_jd = jd_lny;

    for (int i = 0; i < total_months; i++) {
        int month_days = ((info->months_map >> (total_months - 1 - i)) & 1) ? 30 : 29;

        if (days_diff < month_days) {
            *ld = (int)days_diff + 1;
            *lm = current_month;
            *is_leap = is_leap_month_active;
            *m_days = month_days;
            *m_start_jd = accumulated_jd; // Save Julian date for day 1 of this month
            return;
        }

        days_diff -= month_days;
        accumulated_jd += month_days;

        if (has_leap && current_month == info->leap_mon && !is_leap_month_active) {
            is_leap_month_active = 1;
        } else {
            current_month++;
            is_leap_month_active = 0;
        }
    }
}

// Convert Julian Day back to basic Solar elements for grid parsing
void julian_to_solar(long jd, int *d, int *m, int *y) {
    long Q = jd + 0.5; int Z = (int)Q;
    int W = (int)((Z - 1867216.25) / 36524.25); int X = W / 4; int A = Z + 1 + W - X;
    int B = A + 1524; int C = (int)((B - 122.1) / 365.25); int D = (int)(365.25 * C);
    int E = (int)((B - D) / 30.6001);
    *d = B - D - (int)(30.6001 * E);
    *m = (E < 14) ? E - 1 : E - 13;
    *y = (*m > 2) ? C - 4716 : C - 4715;
}

int main() {
    time_t t = time(NULL);
    struct tm tm = *localtime(&t);
    int sd = tm.tm_mday; int sm = tm.tm_mon + 1; int sy = tm.tm_year + 1900;

    int ld, lm, ly, is_leap, total_days_in_lunar_month;
    long month_start_jd;
    solar_to_lunar(sd, sm, sy, &ld, &lm, &ly, &is_leap, &total_days_in_lunar_month, &month_start_jd);

    char can_chi_year[50];
    int can_idx = (ly + 6) % 10; int chi_idx = (ly + 8) % 12;
    sprintf(can_chi_year, "%s %s", CAN[can_idx], CHI[chi_idx]);

    // Determine what weekday the 1st day of the lunar month falls on
    int first_day_s_d, first_day_s_m, first_day_s_y;
    julian_to_solar(month_start_jd, &first_day_s_d, &first_day_s_m, &first_day_s_y);
    int start_weekday = get_weekday(first_day_s_d, first_day_s_m, first_day_s_y);

    // Display Header
    printf("\n      Tháng %d %s (Năm %s)\n", lm, is_leap ? "[Nhuận]" : "", can_chi_year);
    printf("=========================================\n");
    printf("  CN   T2   T3   T4   T5   T6   T7\n");

    // Print leading empty spaces for the first week layout
    for (int i = 0; i < start_weekday; i++) {
        printf("     ");
    }

    // Print the grid days
    for (int day = 1; day <= total_days_in_lunar_month; day++) {
        // Wrap brackets `[ ]` around today's current date inside the grid
        if (day == ld) {
            printf(" [%2d]", day);
        } else {
            printf("  %2d ", day);
        }

        // Break row at Saturday (6)
        if ((day + start_weekday) % 7 == 0 && day < total_days_in_lunar_month) {
            printf("\n");
        }
    }
    printf("\n=========================================\n");
    printf(" [*] Hôm nay: Ngày %02d Tháng %02d (Âm Lịch)\n\n", ld, lm);

    return 0;
}
