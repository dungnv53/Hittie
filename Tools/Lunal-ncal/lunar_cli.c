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

static void get_can_chi_year(int lunar_year, char *buffer, size_t size)
{
    int can_idx = (lunar_year + 6) % 10;
    int chi_idx = (lunar_year + 8) % 12;
    snprintf(buffer, size, "%s %s", CAN[can_idx], CHI[chi_idx]);
}

int main(void)
{
    time_t t = time(NULL);
    struct tm tm = *localtime(&t);
    int sd = tm.tm_mday;
    int sm = tm.tm_mon + 1;
    int sy = tm.tm_year + 1900;

    struct Gregorian date = lunarh_new_Gregorian(sy, sm, sd, 7.0);
    struct Lunar lunar = lunarh_convert_Gregorian_to_Lunar(date);

    char can_chi_year[50];
    get_can_chi_year(lunar.year, can_chi_year, sizeof(can_chi_year));

    printf("=========================================\n");
    printf("        LUNAR CLI DATE UTILITY           \n");
    printf("=========================================\n");
    printf(" Solar Date : %02d/%02d/%d\n", sd, sm, sy);
    printf("-----------------------------------------\n");
    printf(" Lunar Date : %02d/%02d/%d %s\n", lunar.day, lunar.month,
        lunar.year, lunar.leap ? "(Nhuận)" : "");
    printf(" Year Name  : Năm %s\n", can_chi_year);
    printf("=========================================\n");

    return 0;
}
