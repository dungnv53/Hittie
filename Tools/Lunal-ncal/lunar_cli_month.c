#include <stdio.h>
#include <time.h>
#include "lunar.h"

static const char* CAN[] = {
    "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý"
};
static const char* CHI[] = {
    "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi",
    "Thân", "Dậu", "Tuất", "Hợi"
};

int main(void)
{
    time_t t = time(NULL);
    struct tm tm = *localtime(&t);
    int sd = tm.tm_mday;
    int sm = tm.tm_mon + 1;
    int sy = tm.tm_year + 1900;

    struct Gregorian date = lunarh_new_Gregorian(sy, sm, sd, 7.0);
    struct Lunar lunar = lunarh_convert_Gregorian_to_Lunar(date);
    struct Lunar first = lunarh_new_Lunar(lunar.year, lunar.month, lunar.leap,
        1, 7.0);

    int start_weekday = (first.jd + 1) % 7;
    int month_days = lunar.month_size;

    char can_chi_year[50];
    int can_idx = (lunar.year + 6) % 10;
    int chi_idx = (lunar.year + 8) % 12;
    snprintf(can_chi_year, sizeof(can_chi_year), "%s %s",
        CAN[can_idx], CHI[chi_idx]);

    printf("\n      Tháng %d %s (Năm %s)\n", lunar.month,
        lunar.leap ? "[Nhuận]" : "", can_chi_year);
    printf("=========================================\n");
    printf("  CN   T2   T3   T4   T5   T6   T7\n");

    for (int i = 0; i < start_weekday; i++)
    {
        printf("     ");
    }

    for (int day = 1; day <= month_days; day++)
    {
        if (day == lunar.day)
        {
            printf(" [%2d]", day);
        }
        else
        {
            printf("  %2d ", day);
        }

        if ((day + start_weekday) % 7 == 0 && day < month_days)
        {
            printf("\n");
        }
    }
    printf("\n=========================================\n");
    printf(" [*] Hôm nay: Ngày %02d Tháng %02d (Âm Lịch)\n\n",
        lunar.day, lunar.month);

    return 0;
}
