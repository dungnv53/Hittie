// TODO tim moi lien he giua cac doan ma lam thay doi cac thong so 
// hero enermy ... --> dan toi anh huong toi ham AI
// Ko thi doan xem khi hero x,y thay doi , enemy x, y thay doi thi ham nay se tra ve data the nao

public void e_attack_ai(int i) {
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        if(i > 100) {
            i1 = h_x - e_boss_x;
            k = i;
        } else
        {
            i1 = h_x - e_x[i];
            l = i;
        }
        if(i1 >= -9 && i1 <= -6)
        {
            if(e_lv[l] >= 2 || k > 100)
            {
                int j1 = get_random(3);
                if(j1 == 0)
                    j = -2;
                else
                if(j1 == 1)
                    j = -3;
                else
                    j = -4;
            } else {
                j = get_random1(6);
            }
        } else if(i1 >= -5 && i1 <= -2) {
            if(e_lv[l] >= 2 || k > 100) {
                int k1 = get_random(3);
                if(k1 == 0)
                    j = -1;
                else
                if(k1 == 1)
                    j = -2;
                else
                    j = -3;
            } else {
                j = get_random1(6);
            }
        } else if(i1 >= -1 && i1 <= 1) {
            if(e_lv[l] >= 2 || k > 100) {
                int l1 = get_random(3);
                if(l1 == 0)
                    j = -1;
                else
                if(l1 == 1)
                    j = 0;
                else
                    j = 1;
            } else {
                j = get_random1(6);
            }
        } else if(i1 >= 2 && i1 <= 5) {
            if(e_lv[l] >= 2 || k > 100) {
                int i2 = get_random(3);
                if(i2 == 0)
                    j = 1;
                else
                if(i2 == 1)
                    j = 2;
                else
                    j = 3;
            } else {
                j = get_random1(6);
            }
        } else if(i1 >= 6 && i1 <= 9) {
            if(e_lv[l] >= 2 || k > 100) {
                int j2 = get_random(3);
                if(j2 == 0)
                    j = 2;
                else
                if(j2 == 1)
                    j = 3;
                else
                    j = 4;
            } else {
                j = get_random1(6);
            }
        } else if(i1 >= 10) {
            if(e_lv[l] >= 2 || k > 100) {
                int k2 = get_random(3);
                if(k2 == 0)
                    j = 4;
                else
                    j = 5;
            } else {
                j = get_random1(6);
            }
        } else
        if(i1 <= -10)
            if(e_lv[l] >= 2 || k > 100) {
                int l2 = get_random(2);
                if(l2 == 0)
                    j = -4;
                else
                    j = -5;
            } else {
                j = get_random1(6);
            }
        if(k < 100) {
            if(e_lv[l] >= 2) {
                int i3 = get_random(4);
                if(i3 == 1) {
                    int i4 = get_random(3);
                    if(i4 == 0)
                        e_wp[l] = 1;
                    else
                    if(i4 == 1)
                        e_wp[l] = 2;
                    else
                        e_wp[l] = 3;
                } else {
                    e_wp[l] = 0;
                }
            }
            e_behv[l] = j;
            e_snow_y[l] = e_y[l];
            e_snow_x[l] = e_x[l] * 5;
            e_snow_gap[l] = 0;
            e_snow_dx[l] = j;
            e_snow_top[l] = 1;
            e_idx[l] = 3;
            dis_count[l] = 1;
        } else {
            if(k < 103) {
                int j3 = get_random(2);
                if(j3 == 0) {
                    int k3 = get_random(3);
                    if(k3 == 0)
                        e_boss_wp = 1;
                    else
                    if(k3 == 1)
                        e_boss_wp = 2;
                    else
                        e_boss_wp = 3;
                } else {
                    e_boss_wp = 0;
                }
            } else {
                int l3 = get_random(3);
                if(l3 == 0)
                    e_boss_wp = 1;
                else
                if(l3 == 1)
                    e_boss_wp = 2;
                else
                    e_boss_wp = 3;
            }
            e_boss_behv = j;
            e_boss_snow_y = e_boss_y;
            e_boss_snow_x = e_boss_x * 5;
            e_boss_snow_gap = 0;
            e_boss_snow_dx = j;
            e_boss_snow_top = 1;
            e_boss_idx = 3;
            boss_dis_count = 1;
        }
    }

    if(e_time == e_fire_time[i] && get_random(3) != 1 && e_ppang_item[i] != 2)
        e_attack_ai(i);
    // 102, 101