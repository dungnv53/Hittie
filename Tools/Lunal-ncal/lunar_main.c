#include <stdio.h>
#include "lunar.h"

int main(int argc, char** argv)
{
    struct ArgumentList argl = lunarh_parse_argv(argc, argv);
    if (argl.valid == 0)
    {
        printf("Invalid arguments\n");
        return 1;
    }
    if (argl.task == LUNARH_GREGORIAN_TO_LUNAR_IN_CHINESE)
    {
        lunarh_display_Lunar_in_Chinese(argl);
    }
    else if (argl.task == LUNARH_GREGORIAN_TO_LUNAR_IN_MODERN_NOTATION)
    {
        lunarh_display_Lunar_in_modern_notation(argl);
    }
    else if (argl.task == LUNARH_LUNAR_TO_GREGORIAN)
    {
        lunarh_display_Gregorian(argl);
    }
    else if (argl.task == LUNARH_LUNAR_CALENDAR_OF_A_YEAR)
    {
        lunarh_display_Lunar_calendar_of_a_year(argl);
    }
    else
    {
        printf("Unrecognizable task: %d\n", argl.task);
        return 1;
    }
}
