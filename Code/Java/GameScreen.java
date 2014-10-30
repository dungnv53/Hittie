// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.samsung.util.AudioClip;
import com.samsung.util.Vibration;
import java.io.*;
import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

class GameScreen extends Canvas implements Runnable {

    // construct()
    public GameScreen(SnowBallFight snowballfight) {
        recordStore = null;
        game_state = 0;
        saved_gold = 10;
        speed = 4;
        game_speed = 17;
        rnd = new Random();
        thread = null;
        screen = -1;
        gameOn = true;
        m_mode = 1;
        s_play = 1;
        v_mode = 1;
        audioClip = null;
        last_stage = 11;
        mana = 0;
        item_slot = new int[5];
        item_price = new int[8];
        del = -1;
        item_c_num = 2;
        SJ = snowballfight;
        item_price[0] = 5;
        item_price[1] = 8;
        item_price[2] = 8;
        item_price[3] = 14;
        item_price[4] = 6;
        item_price[5] = 12;
        item_price[6] = 10;
        item_price[7] = 12;
        printScore("hero", 0);
        printScore("config", 1);
        item_slot[0] = 3;
        item_slot[1] = 5;
        stage = last_stage;
    }

    public void addScore(String s, int i) {
        // Luu điểm vào file.
        int j = saved_gold * 10000 + last_stage * 100 + mana;
        // lưu config speed ...
    }

    public void printScore(String s, int i) {
        last_stage = 11;
        saved_gold = 0;
        mana = 0;
        // new game.
        // speed
        int j = datainputstream.readInt();
        last_stage = (j % 10000) / 100;
        saved_gold = j / 10000;
        mana = j % 100;
    }

    public void loadImage(int i) {
        try
        {
            if(i == 2) {
                imgMM = Image.createImage("/mm.png");
                imgBk = Image.createImage("/bk.png");
                imgSl = Image.createImage("/sl.png");
                imgPl = Image.createImage("/play.png");
                imgCh = Image.createImage("/check.png");
            } else if(i == 6) {
                imgHero = new Image[5];
                imgEnemy = new Image[4];
                imgItem = new Image[9];
                imgItem_hyo = new Image[2];
                imgItem_hyo[0] = Image.createImage("/hyo0.png");
                imgItem_hyo[1] = Image.createImage("/hyo1.png");
                for(int j = 0; j < 5; j++)
                    imgHero[j] = Image.createImage("/hero" + j + ".png");

                if(get_random(2) == 0) {
                    for(int k1 = 0; k1 < 4; k1++)
                        imgEnemy[k1] = Image.createImage("/enemy0" + k1 + ".png");

                } else {
                    for(int l1 = 0; l1 < 4; l1++)
                        imgEnemy[l1] = Image.createImage("/enemy1" + l1 + ".png");

                }
                for(int i2 = 0; i2 < 9; i2++)
                    imgItem[i2] = Image.createImage("/item" + i2 + ".png");

                System.gc();
                imgSnow_g = Image.createImage("/snow_gauge.png");
                imgPwd = Image.createImage("/power.png");
                imgShadow = Image.createImage("/shadow0.png");
                imgPok = Image.createImage("/pok.png");
                imgPPang = Image.createImage("/bbang0.png");
                imgPPang1 = Image.createImage("/bbang1.png");
                imgH_ppang = Image.createImage("/h_bbang.png");
                imgCh = Image.createImage("/check.png");
                imgAl = Image.createImage("/al.png");
                imgEffect = new Image[2];
                imgEffect[0] = Image.createImage("/effect0.png");
                imgEffect[1] = Image.createImage("/effect1.png");
            } else if(i == 100)
                imgBack = Image.createImage("/back" + school + ".png");
            else if(i == 7) {
                imgBoss = new Image[4];
                for(int k = 0; k < 4; k++)
                    imgBoss[k] = Image.createImage("/boss" + e_boss + k + ".png");

            } else if(i == -6) {
                imgStage = new Image[5];
                for(int l = 0; l < 5; l++)
                    imgStage[l] = Image.createImage("/word-" + l + ".png");

                imgStage_num = Image.createImage("/stage" + tmp_stage + ".png");
            } else if(i == 8) {
                imgSpecial = new Image[3];
                for(int i1 = 0; i1 < 3; i1++)
                    imgSpecial[i1] = Image.createImage("/special" + i1 + ".png");

                gameOn = true;
            } else if(i == 9)
                imgSp = Image.createImage("/sp" + special + ".png");
            else if(i == 3) {
                imgVill = Image.createImage("/village.png");
                imgCh = Image.createImage("/hero_icon.png");
                imgSchool = Image.createImage("/school.png");
            } else if(i == 31) {
                if(m_mode == 1)
                    imgShop = Image.createImage("/shop0.png");
                if(m_mode == 0)
                    imgShop = Image.createImage("/shop1.png");
            } else if(i == 200) {
                imgVictory = Image.createImage("/victory.png");
                imgV = Image.createImage("/v.png");
                imgHero_v = Image.createImage("/hero-vic.png");
            } else if(i == -200) {
                imgLose = Image.createImage("/lose.png");
                imgHero_l = Image.createImage("/hero-lose.png");
            } else if(i == 1) {
                imgNum = new Image[10];
                for(int j1 = 0; j1 < 10; j1++)
                    imgNum[j1] = Image.createImage("/" + j1 + ".png");

                imgLogo = Image.createImage("/logo.png");
            }
        }
        catch(Exception exception) { }
    }

    public void destroyImage(int i) {
        if(i == 1)
            imgLogo = null;
        else if(i == 2)
        {
            imgMM = null;
            imgBk = null;
            imgPl = null;
            imgSl = null;
            imgCh = null;
        } else
        if(i == 3)
        {
            imgVill = null;
            imgCh = null;
            imgSchool = null;
        } else
        if(i == 31)
            imgShop = null;
        else
        if(i == 6)
        {
            imgHero = null;
            imgEnemy = null;
            imgItem = null;
            imgSnow_g = null;
            imgPwd = null;
            imgShadow = null;
            imgPok = null;
            imgPPang = null;
            imgPPang1 = null;
            imgH_ppang = null;
            imgItem_hyo = null;
            imgCh = null;
            imgAl = null;
            imgEffect = null;
        } else
        if(i == 100)
            imgBack = null;
        else
        if(i == -6)
        {
            imgStage = null;
            imgStage_num = null;
        } else
        if(i == 7)
            imgBoss = null;
        else
        if(i == 8)
            imgSpecial = null;
        else
        if(i == 9)
            imgSp = null;
        else
        if(i == 200)
        {
            imgVictory = null;
            imgV = null;
            imgHero_v = null;
        } else
        if(i == -200)
        {
            imgLose = null;
            imgHero_l = null;
        }
        System.gc();
    }

    public void init_game(int i)
    {
        screen = 77;
        repaint();
        serviceRepaints();
        game_state = 0;
        p_mode = 1;
        h_x = 5;
        h_y = 8;
        h_idx = 0;
        max_hp = 106;
        hp = max_hp;
        wp = 0;
        pw_up = 0;
        snow_pw = 0;
        real_snow_pw = 0;
        dem = 12;
        ppang = 0;
        al = -1;
        ppang_time = 0;
        ppang_item = 0;
        make_enemy(i);
        d_gauge = 2;
        screen = 6;
        item_mode = 0;
        loadImage(6);
        loadImage(100);
        if(e_boss > 0)
            loadImage(7);
        state = 2;
        ani_step = 0;
        startThread();
        gameOn = true;
    }

    /*
    Create enermy, box number, number of enermy increase by level (i);
    Otherwise, this number could be enermy code (level of enermy or box).
    */
    public void make_e_num(int i, int j) {
        if(j == 1){
            if(i == 1)
            {
                e_boss = 0;
                e_num = 2;
            } else
            if(i == 2)
            {
                e_boss = 0;
                e_num = 2;
            } else
            if(i == 3)
            {
                e_boss = 1;
                e_num = 2;
            } else
            if(i == 4)
            {
                e_boss = 3;
                e_num = 2;
            }
        } else if(j == 2) {
            if(i == 1)
            {
                e_boss = 0;
                e_num = 2;
            } else
            if(i == 2)
            {
                e_boss = 0;
                e_num = 3;
            } else
            if(i == 3)
            {
                e_boss = 2;
                e_num = 2;
            } else
            if(i == 4)
            {
                e_boss = 3;
                e_num = 3;
            }
        } else
        if(j == 3) {
            if(i == 1)
            {
                e_boss = 0;
                e_num = 3;
            } else
            if(i == 2)
            {
                e_boss = 2;
                e_num = 2;
            } else
            if(i == 3)
            {
                e_boss = 0;
                e_num = 4;
            } else
            if(i == 4)
            {
                e_boss = 3;
                e_num = 4;
            }
        } else
        if(j == 4)
            if(i == 1)
            {
                e_boss = 1;
                e_num = 3;
            } else
            if(i == 2)
            {
                e_boss = 2;
                e_num = 3;
            } else
            if(i == 3)
            {
                e_boss = 3;
                e_num = 4;
            } else
            if(i == 4)
            {
                e_boss = 4;
                e_num = 4;
            }
        e_t_num = e_num;
        tmp_stage = i;
    }

    public void make_enemy(int i) {
        if(i < 0)
            make_e_num(get_random(2) + 2, school);
        else
            make_e_num(last_stage % 10, school);
        e_x = new int[e_num];
        e_y = new int[e_num];
        e_hp = new int[e_num];
        max_e_hp = new int[e_num];
        e_lv = new int[e_num];
        e_idx = new int[e_num];
        e_behv = new int[e_num];
        e_snow_y = new int[e_num];
        e_snow_x = new int[e_num];
        e_snow_gap = new int[e_num];
        e_snow_top = new int[e_num];
        e_snow_dx = new int[e_num];
        e_fire_time = new int[e_num];
        e_wp = new int[e_num];
        e_ppang_item = new int[e_num];
        e_ppang_time = new int[e_num];
        e_move_dir = new int[e_num];
        dis_count = new int[e_num];
        e_time = 0;
        for(int j = 0; j < e_num; j++) {
            if(school == 1 || school == 2)
                e_hp[j] = 20 + school * 10;
            else
            if(school == 3)
                e_hp[j] = 54;
            else
            if(school == 4)
                e_hp[j] = 66;
            max_e_hp[j] = e_hp[j];
            e_snow_y[j] = -10;
            e_behv[j] = 100;
            e_wp[j] = 0;
        }

        if(school < 3)
            e_dem = school + 7;
        else
        if(school == 3)
            e_dem = school + 9;
        else
            e_dem = 14;
        e_x[0] = 3 + get_random(3);
        e_y[0] = 1 + get_random(3);
        e_lv[0] = 3;
        e_fire_time[0] = 8;
        e_x[1] = 18 + get_random(3);
        e_y[1] = 1 + get_random(3);
        e_lv[1] = 3;
        e_fire_time[1] = 17;
        if(e_t_num >= 3)
        {
            e_x[2] = 13 + get_random(3);
            e_y[2] = 3 + get_random(2);
            e_lv[2] = 3;
            e_fire_time[2] = 20;
        }
        if(e_t_num == 4)
        {
            e_x[3] = 8;
            e_y[3] = 5;
            e_lv[3] = 3;
            e_fire_time[3] = 4;
        }
        e_boss_behv = 100;
        e_boss_snow_y = -10;
        e_boss_x = 10;
        e_boss_y = 6;
        e_boss_idx = 0;
        e_boss_hp = e_boss * 10 + 30 + (school - 1) * 10;
        max_e_boss_hp = e_boss_hp;
        e_boss_fire_time = 2;
    }

    public void paint(Graphics g) {
        if(screen == 6)
        {
            g.drawImage(imgBack, 0, 0, 20);
            g.setColor(0xffffff);
            g.fillRect(0, 25, 128, 84);
            Graphics _tmp = g;
            Graphics _tmp1 = g;
            g.drawImage(imgHero[h_idx], h_x * 5, 83, 0x10 | 1);
            if(ppang_time > 0)
            {
                if(ppang_item == 1)
                {
                    Graphics _tmp2 = g;
                    Graphics _tmp3 = g;
                    g.drawImage(imgItem_hyo[0], h_x * 5, 74, 0x10 | 1);
                } else
                {
                    Graphics _tmp4 = g;
                    Graphics _tmp5 = g;
                    g.drawImage(imgItem_hyo[1], h_x * 5, 83, 0x10 | 1);
                }
                ppang_time--;
                if(ppang_time == 0)
                    ppang_item = 0;
            }
            draw_enemy(g);
            if(item_mode != 0)
            {
                g.setColor(0xc1c1c1);
                if(message != "")
                    draw_text(g);
                for(int i = 1; i <= 5; i++)
                    g.drawRect(i * 12 + 23, 110, 10, 9);

                if(item_mode != 100)
                {
                    g.setColor(0xff0000);
                    g.drawRect(item_mode * 12 + 23, 110, 10, 9);
                } else
                if(item_mode == 100)
                    item_mode = 0;
            }
            if(pw_up == 2)
            {
                Graphics _tmp6 = g;
                Graphics _tmp7 = g;
                g.drawImage(imgShadow, snow_x * 5, snow_y * 7 + 4, 2 | 1);
                Graphics _tmp8 = g;
                Graphics _tmp9 = g;
                g.drawImage(imgItem[wp], snow_x * 5, (snow_y * 7 - snow_gap) + 4, 2 | 1);
            } else
            if(pw_up == 1)
            {
                if(real_snow_pw > 0 && ppang_item != 1)
                {
                    g.setColor(0x6dcff6);
                    if(h_x >= 13)
                    {
                        g.fillRect(h_x * 5 - 16, 106 - real_snow_pw * 3, 3, real_snow_pw * 3);
                        Graphics _tmp10 = g;
                        Graphics _tmp11 = g;
                        g.drawImage(imgPwd, h_x * 5 - 15, 83, 0x10 | 1);
                    } else
                    {
                        g.fillRect(h_x * 5 + 14, 106 - real_snow_pw * 3, 3, real_snow_pw * 3);
                        Graphics _tmp12 = g;
                        Graphics _tmp13 = g;
                        g.drawImage(imgPwd, h_x * 5 + 15, 83, 0x10 | 1);
                    }
                }
            } else
            if(pw_up == 0)
            {
                if(ppang <= -1)
                {
                    Graphics _tmp14 = g;
                    Graphics _tmp15 = g;
                    g.drawImage(imgPok, snow_x * 5, snow_y * 7 - 3, 2 | 1);
                    ppang--;
                    if(ppang == -3)
                        ppang = 0;
                } else
                if(ppang >= 1 && ppang <= 10)
                {
                    if(s_item != -10)
                    {
                        if(ppang < 3)
                        {
                            Graphics _tmp16 = g;
                            Graphics _tmp17 = g;
                            g.drawImage(imgPPang, snow_x * 5, snow_y * 7 - 6, 2 | 1);
                        } else
                        {
                            Graphics _tmp18 = g;
                            Graphics _tmp19 = g;
                            g.drawImage(imgPPang1, snow_x * 5, snow_y * 7 - 6, 2 | 1);
                        }
                    } else
                    if(ppang < 4)
                    {
                        Graphics _tmp20 = g;
                        Graphics _tmp21 = g;
                        g.drawImage(imgEffect[0], snow_x * 5, snow_y * 7 - 2, 2 | 1);
                    } else
                    {
                        Graphics _tmp22 = g;
                        Graphics _tmp23 = g;
                        g.drawImage(imgEffect[1], snow_x * 5, snow_y * 7 - 2, 2 | 1);
                    }
                    if(hit_idx != 10)
                    {
                        if(e_hp[hit_idx] > 0)
                        {
                            g.setColor(0xff0000);
                            g.fillRect(e_x[hit_idx] * 5 + 8, e_y[hit_idx] * 5 + 5, 3, 15);
                            g.setColor(0x93959a);
                            g.fillRect(e_x[hit_idx] * 5 + 8, e_y[hit_idx] * 5 + 5, 3, 15 - (15 * e_hp[hit_idx]) / max_e_hp[hit_idx]);
                        }
                    } else
                    if(hit_idx == 10)
                    {
                        if(e_boss_hp > 0)
                        {
                            g.setColor(0xff0000);
                            g.fillRect(e_boss_x * 5 + 12, e_boss_y * 5 + 5, 3, 15);
                            g.setColor(0x93959a);
                            g.fillRect(e_boss_x * 5 + 12, e_boss_y * 5 + 5, 3, 15 - (15 * e_boss_hp) / max_e_boss_hp);
                        }
                        if(al == 1)
                        {
                            Graphics _tmp24 = g;
                            Graphics _tmp25 = g;
                            g.drawImage(imgAl, snow_x * 5 + 6, snow_y * 7 - 10, 2 | 1);
                        }
                    }
                    ppang++;
                    if(ppang == 6)
                    {
                        ppang = 0;
                        s_item = 0;
                        al = -1;
                    }
                } else
                if(ppang >= 50)
                    draw_sp_hyo(g);
                if(message != "")
                    draw_text(g);
            } else
            if(pw_up == -1)
                pw_up = 0;
            if(p_mode == 1)
            {
                try
                {
                    g.drawImage(Image.createImage("/ui.png"), 0, 109, 20);
                }
                catch(Exception exception) { }
                draw_item(g);
                p_mode = 2;
                System.gc();
            }
            if(d_gauge != 0)
                draw_gauge(g);
            for(int j = 0; j < e_num; j++)
                if(e_behv[j] != 100)
                {
                    Graphics _tmp26 = g;
                    Graphics _tmp27 = g;
                    g.drawImage(imgShadow, e_snow_x[j], e_snow_y[j] * 6 + 17, 2 | 1);
                    Graphics _tmp28 = g;
                    Graphics _tmp29 = g;
                    g.drawImage(imgItem[e_wp[j]], e_snow_x[j], (e_snow_y[j] * 6 + 13) - e_snow_gap[j], 2 | 1);
                }

            if(e_boss_behv != 100 && e_boss > 0)
            {
                Graphics _tmp30 = g;
                Graphics _tmp31 = g;
                g.drawImage(imgShadow, e_boss_snow_x, e_boss_snow_y * 6 + 17, 2 | 1);
                Graphics _tmp32 = g;
                Graphics _tmp33 = g;
                g.drawImage(imgItem[e_boss_wp], e_boss_snow_x, (e_boss_snow_y * 6 + 13) - e_boss_snow_gap, 2 | 1);
            }
            if(del != -1)
                draw_item(g);
            if(h_timer_p <= -1)
                if(h_timer_p != -5)
                {
                    Graphics _tmp34 = g;
                    Graphics _tmp35 = g;
                    g.drawImage(imgH_ppang, h_x * 5 + 1, 81, 2 | 1);
                    h_timer_p--;
                } else
                if(h_timer_p == -5)
                {
                    h_timer_p = 0;
                    g.setColor(0xff0000);
                    g.fillRect(5, 113, 9, 12);
                    g.setColor(0x8e8e8e);
                    if(hp > 0)
                        g.fillRect(5, 113, 9, 12 - (12 * hp) / max_hp);
                    if(hp <= 0)
                    {
                        state = 3;
                        game_state = 1;
                        gameOn = true;
                    }
                }
            if(state == 2)
            {
                if(ani_step >= 3)
                    g.drawImage(imgStage[0], 20, 60, 20);
                if(ani_step >= 6)
                    g.drawImage(imgStage[1], 35, 60, 20);
                if(ani_step >= 9)
                    g.drawImage(imgStage[2], 50, 60, 20);
                if(ani_step >= 12)
                    g.drawImage(imgStage[3], 65, 60, 20);
                if(ani_step >= 15)
                    g.drawImage(imgStage[4], 80, 60, 20);
                if(ani_step >= 19)
                    g.drawImage(imgStage_num, 95, 60, 20);
            }
        } else
        if(screen == 2)
        {
            g.drawImage(imgMM, 0, 0, 20);
            g.setColor(0xffffcc);
            g.drawString("1.Play", 13, 23, 20);
            g.drawString("2.Instructions", 13, 38, 20);
            g.drawString("3.Configuration", 13, 53, 20);
            g.drawString("4.Quit", 13, 68, 20);
            g.drawImage(imgSl, 68, 115, 20);
            g.drawImage(imgCh, 3, m_mode * 15 + 11, 20);
        } else
        if(screen == 3)
        {
            g.drawImage(imgVill, 0, 0, 20);
            g.setColor(0xe4cbae);
            if(last_stage / 10 == 1)
            {
                g.drawImage(imgSchool, 78, 87, 3);
                g.drawImage(imgSchool, 49, 87, 3);
                g.drawImage(imgSchool, 19, 58, 3);
                g.fillRect(76, 73, 6, 5);
                g.fillRect(47, 73, 6, 5);
                g.setColor(0xe6e6e6);
                g.fillRect(17, 44, 6, 5);
            } else
            if(last_stage / 10 == 2)
            {
                g.drawImage(imgSchool, 49, 87, 3);
                g.drawImage(imgSchool, 19, 58, 3);
                g.setColor(0xe4cbae);
                g.fillRect(47, 73, 6, 5);
                g.setColor(0xe6e6e6);
                g.fillRect(17, 44, 6, 5);
            } else
            if(last_stage / 10 == 3)
            {
                g.drawImage(imgSchool, 19, 58, 3);
                g.setColor(0xe6e6e6);
                g.fillRect(17, 44, 6, 5);
            }
            g.drawImage(imgCh, h_x, h_y, 20);
            if(m_mode != -1)
            {
                if(m_mode == 0)
                    message = "Drugstore";
                else
                if(m_mode == 1)
                    message = "Item Shop";
                else
                if(m_mode == 2)
                    message = "Eastern Boys";
                else
                if(m_mode == 3)
                    message = "Southern Boys";
                else
                if(m_mode == 4)
                    message = "Western Boys";
                else
                if(m_mode == 5)
                    message = "Northern Boys";
                else
                if(m_mode == 100)
                    message = "No Admittance";
                if(message != "")
                    draw_text(g);
            }
            if(ani_step == 0 && last_stage > 20)
            {
                if(last_stage == 31)
                    draw_text_box(g, "Western Boys");
                else
                if(last_stage == 41)
                    draw_text_box(g, "Northern Boys");
                else
                if(last_stage == 21)
                    draw_text_box(g, "Southern Boys");
                ani_step++;
            }
        } else
        if(screen == 31)
        {
            g.drawImage(imgShop, 24, 20, 20);
            g.setColor(0xffff66);
            g.drawRect(27, s_item * 13 + 30, 29, 10);
            g.drawRect(28, s_item * 13 + 31, 27, 8);
            g.setColor(0xccff99);
            g.drawRect(b_item * 16 + 32, 70, 15, 15);
            g.drawRect(b_item * 16 + 33, 71, 13, 13);
            draw_int(g, saved_gold, 84, 96);
            if(m_mode == 1)
                draw_int(g, item_price[b_item], 42, 96);
            else
            if(m_mode == 0)
                draw_int(g, item_price[b_item + 4], 42, 96);
            if(message != "")
                draw_text(g);
        } else
        if(screen == 100)
        {
            g.setColor(0xffffff);
            g.fillRect(1, 20, 126, 90);
            g.setColor(0);
            g.drawRect(0, 19, 127, 90);
            g.drawRect(0, 21, 127, 86);
            g.drawImage(imgCh, 3, m_mode * 14 + 18, 20);
            g.drawString("Resume", 15, 28, 20);
            g.drawString("MainMenu", 15, 42, 20);
            g.drawString("Sound", 15, 56, 20);
            if(s_play == 1)
            {
                g.setColor(255);
                g.drawString("On/", 69, 56, 20);
                g.setColor(0x808080);
                g.drawString("off", 96, 56, 20);
            } else
            {
                g.setColor(0x808080);
                g.drawString("on/", 69, 56, 20);
                g.setColor(255);
                g.drawString("OFF", 93, 56, 20);
            }
            g.setColor(0);
            g.drawString("Instructions", 15, 70, 20);
            g.drawString("Quit", 15, 84, 20);
        } else
        if(screen == -88)
        {
            g.drawImage(imgMM, 0, 0, 20);
            g.setColor(0xffffcc);
            g.drawString("1.New Game", 13, 27, 20);
            g.drawString("2.Saved Game", 13, 44, 20);
            g.drawImage(imgSl, 68, 115, 20);
            g.drawImage(imgBk, 2, 115, 20);
            g.drawImage(imgCh, 4, m_mode * 17 + 14, 20);
        } else
        if(screen == 8)
        {
            if(ani_step == 1 || ani_step == 2)
            {
                g.setColor(10173);
                g.fillRect(0, 40, 128, 60);
                g.drawImage(imgSpecial[0], 44, 70, 3);
                g.drawImage(imgSpecial[1], 44, 89, 3);
            } else
            if(ani_step == 8)
            {
                g.drawImage(imgSpecial[0], 44, 70, 3);
                g.drawImage(imgSpecial[1], 48, 89, 3);
            } else
            if(ani_step == 16)
            {
                g.drawImage(imgSpecial[0], 44, 70, 3);
                g.drawImage(imgSpecial[1], 51, 89, 3);
            } else
            if(ani_step == 23)
            {
                g.drawImage(imgSpecial[0], 44, 70, 3);
                g.drawImage(imgSpecial[1], 54, 89, 3);
            } else
            if(ani_step == 30)
            {
                g.drawImage(imgSpecial[0], 44, 70, 3);
                g.drawImage(imgSpecial[1], 55, 89, 3);
            } else
            if(ani_step == 37)
                g.drawImage(imgSpecial[2], 58, 88, 3);
            else
            if(ani_step == 50)
            {
                destroyImage(8);
                loadImage(9);
                ani_step = 0;
                screen = 9;
            }
        } else
        if(screen == 9)
        {
            if(ani_step == 1 || ani_step == 46)
            {
                if(ani_step == 46)
                {
                    destroyImage(9);
                    loadImage(100);
                    pw_up = -1;
                    g.drawImage(imgBack, 0, 0, 20);
                    screen = 6;
                    ppang = 50;
                    for(int k = 0; k < e_num; k++)
                    {
                        e_move_dir[k] = 0;
                        decs_e_hp(k);
                    }

                    if(e_boss > 0)
                    {
                        e_boss_move_dir = 0;
                        decs_e_hp(10);
                    }
                    dem = 12;
                    mana = 0;
                }
                g.setColor(0xffffff);
                g.fillRect(0, 25, 128, 84);
                for(int l = 0; l < e_num; l++)
                {
                    if(e_x[l] != -10)
                    {
                        Graphics _tmp36 = g;
                        Graphics _tmp37 = g;
                        g.drawImage(imgEnemy[e_idx[l]], e_x[l] * 5, e_y[l] * 5 + 5, 0x10 | 1);
                    }
                    if(e_behv[l] != 100)
                    {
                        Graphics _tmp38 = g;
                        Graphics _tmp39 = g;
                        g.drawImage(imgShadow, e_snow_x[l], e_snow_y[l] * 6 + 17, 2 | 1);
                        Graphics _tmp40 = g;
                        Graphics _tmp41 = g;
                        g.drawImage(imgItem[e_wp[l]], e_snow_x[l], (e_snow_y[l] * 6 + 13) - e_snow_gap[l], 2 | 1);
                    }
                }

                if(e_boss > 0)
                {
                    Graphics _tmp42 = g;
                    Graphics _tmp43 = g;
                    g.drawImage(imgBoss[e_boss_idx], e_boss_x * 5, e_boss_y * 5, 0x10 | 1);
                }
            }
            if(special == 1)
            {
                if(ani_step <= 45)
                    g.drawImage(imgSp, 158 - ani_step * 3, 0, 20);
            } else
            if(special == 2)
            {
                if(ani_step <= 45)
                    g.drawImage(imgSp, 158 - ani_step * 3, 0, 20);
            } else
            if(special == 3 && ani_step <= 45)
                g.drawImage(imgSp, 168 - ani_step * 3, 30, 20);
            Graphics _tmp44 = g;
            Graphics _tmp45 = g;
            g.drawImage(imgHero[0], h_x * 5, 83, 0x10 | 1);
        } else
        if(screen == 4)
        {
            g.drawImage(imgMM, 0, 0, 20);
            g.setColor(0xffffcc);
            g.drawString("Sound", 12, 23, 20);
            if(s_play == 1)
            {
                g.drawString("ON /", 62, 23, 20);
                g.setColor(0xa4a4a4);
                g.drawString("off", 95, 23, 20);
                g.setColor(0xffffcc);
            }
            if(s_play == 2)
            {
                g.setColor(0xa4a4a4);
                g.drawString("on /", 62, 23, 20);
                g.setColor(0xffffcc);
                g.drawString("OFF", 94, 23, 20);
            }
            g.drawString("Vibration ", 12, 41, 20);
            if(v_mode == 1)
            {
                g.drawString("ON /", 62, 59, 20);
                g.setColor(0xa4a4a4);
                g.drawString("off", 95, 59, 20);
                g.setColor(0xffffcc);
            }
            if(v_mode == 2)
            {
                g.setColor(0xa4a4a4);
                g.drawString("on /", 62, 59, 20);
                g.setColor(0xffffcc);
                g.drawString("OFF", 94, 59, 20);
            }
            g.drawString("Speed ", 14, 77, 20);
            g.drawString("[ " + String.valueOf(speed) + " ]", 68, 77, 20);
            g.drawImage(imgBk, 2, 115, 20);
            if(m_mode < 3)
                g.drawImage(imgCh, 4, m_mode * 18 + 9, 20);
            else
                g.drawImage(imgCh, 4, m_mode * 18 + 27, 20);
        } else
        if(screen == 5)
        {
            g.drawImage(imgMM, 0, 0, 20);
            g.setColor(0xffffcc);
            g.drawString("1.Control Keys", 10, 25, 20);
            g.drawString("2.Offense items", 10, 42, 20);
            g.drawString("3.Defense items", 10, 59, 20);
            g.drawImage(imgCh, 3, m_mode * 17 + 12, 20);
            g.drawImage(imgSl, 68, 115, 20);
            g.drawImage(imgBk, 2, 115, 20);
        } else
        if(screen == -33)
        {
            g.drawImage(imgMM, 0, 0, 20);
            g.drawImage(imgBk, 2, 115, 20);
            destroyImage(2);
            g.setColor(0xffffcc);
            try
            {
                if(m_mode == 1)
                    g.drawImage(Image.createImage("/txt4.png"), 5, 25, 20);
                if(m_mode == 2)
                {
                    g.fillRect(6, 23, 10, 10);
                    g.fillRect(6, 45, 10, 10);
                    g.fillRect(6, 61, 10, 10);
                    g.fillRect(6, 84, 10, 10);
                    g.drawImage(Image.createImage("/item1.png"), 7, 24, 20);
                    g.drawImage(Image.createImage("/item2.png"), 7, 46, 20);
                    g.drawImage(Image.createImage("/item3.png"), 7, 62, 20);
                    g.drawImage(Image.createImage("/item4.png"), 7, 85, 20);
                    g.drawImage(Image.createImage("/txt2.png"), 23, 25, 20);
                }
                if(m_mode == 3)
                {
                    g.fillRect(6, 23, 10, 10);
                    g.fillRect(6, 38, 10, 10);
                    g.fillRect(6, 53, 10, 10);
                    g.fillRect(6, 67, 10, 10);
                    g.drawImage(Image.createImage("/item5.png"), 7, 24, 20);
                    g.drawImage(Image.createImage("/item6.png"), 7, 39, 20);
                    g.drawImage(Image.createImage("/item7.png"), 7, 54, 20);
                    g.drawImage(Image.createImage("/item8.png"), 7, 68, 20);
                    g.drawImage(Image.createImage("/txt1.png"), 23, 25, 20);
                }
            }
            catch(Exception exception1) { }
            System.gc();
        } else
        if(screen == 200)
        {
            if(ani_step >= 13 && ani_step < 27)
            {
                g.setColor(0xffffff);
                g.fillRect(0, 60, 128, 47);
                Graphics _tmp46 = g;
                Graphics _tmp47 = g;
                g.drawImage(imgHero_v, h_x * 5, 83, 0x10 | 1);
            } else
            if(ani_step >= 28 && ani_step < 50)
            {
                Graphics _tmp48 = g;
                Graphics _tmp49 = g;
                g.drawImage(imgV, h_x * 5 + 8, 87, 0x10 | 1);
                if(ani_step > 41)
                {
                    Graphics _tmp50 = g;
                    Graphics _tmp51 = g;
                    g.drawImage(imgVictory, 60, 60, 0x10 | 1);
                }
            } else
            if(ani_step == 50)
                ani_step = -1;
        } else
        if(screen == -201)
        {
            if(ani_step < 30)
            {
                g.setColor(0);
                for(int i1 = 0; i1 < 11; i1++)
                    g.fillRect(0, i1 * 10, ani_step * 4 + 12, 5);

            } else
            if(ani_step < 65)
            {
                g.setColor(0);
                for(int j1 = 0; j1 < 11; j1++)
                    g.fillRect(0, j1 * 10 + 5, (ani_step - 30) * 7 - j1 * 10, 5);

            } else
            if(ani_step >= 65 && ani_step <= 100)
            {
                if(ani_step > 90)
                {
                    Graphics _tmp52 = g;
                    Graphics _tmp53 = g;
                    g.drawImage(imgLose, 60, 60, 0x10 | 1);
                }
                Graphics _tmp54 = g;
                Graphics _tmp55 = g;
                g.drawImage(imgHero_l, h_x * 5, 87, 0x10 | 1);
            }
        } else
        if(screen == 300)
        {
            g.drawImage(imgMM, 0, 0, 20);
            g.setColor(0xffffcc);
            g.drawString("Good Job!", 15, 23, 20);
            g.setColor(0xccff66);
            g.drawString("Acquired", 15, 41, 20);
            g.drawString("Gold:", 48, 57, 20);
            g.drawString(String.valueOf(gold), 92, 57, 20);
            g.setColor(0xffffcc);
            g.drawString("press any key", 10, 83, 20);
            g.drawString("to continue", 37, 97, 20);
        } else
        if(screen == 77)
            draw_text(g);
        else
        if(screen == -1)
        {
            loadImage(1);
            g.drawImage(imgLogo, 0, 0, 20);
            MPlay(0);
            destroyImage(1);
        } else
        if(screen == -2)
        {
            g.setColor(0xffffff);
            g.fillRect(0, 0, 128, 135);
            g.setColor(25054);
            g.fillRect(0, 0, 128, 22);
            g.fillRect(0, 71, 128, 84);
            try
            {
                Graphics _tmp56 = g;
                Graphics _tmp57 = g;
                g.drawImage(Image.createImage("/present.png"), 64, 5, 0x10 | 1);
                Graphics _tmp58 = g;
                Graphics _tmp59 = g;
                g.drawImage(Image.createImage("/sam_logo.png"), 64, 28, 0x10 | 1);
                g.drawImage(Image.createImage("/http1.png"), 7, 77, 20);
                g.drawImage(Image.createImage("/http2.png"), 7, 103, 20);
            }
            catch(Exception exception2) { }
            System.gc();
        } else
        if(screen == 1000)
        {
            g.setColor(0xffffff);
            g.fillRect(0, 25, 120, 85);
            try
            {
                Graphics _tmp60 = g;
                Graphics _tmp61 = g;
                g.drawImage(Image.createImage("/allClear.png"), 64, 10, 0x10 | 1);
            }
            catch(Exception exception3) { }
        } else
        if(screen == -5)
        {
            g.setColor(0xffffff);
            g.fillRect(1, 20, 126, 90);
            g.setColor(0);
            g.drawRect(0, 19, 127, 90);
            g.drawRect(0, 21, 127, 86);
            try
            {
                g.drawImage(Image.createImage("/txt4b.png"), 4, 30, 20);
            }
            catch(Exception exception4) { }
            System.gc();
        } else
        if(screen == 1)
        {
            g.drawImage(imgMM, 0, 0, 20);
            try
            {
                Graphics _tmp62 = g;
                Graphics _tmp63 = g;
                g.drawImage(Image.createImage("/title.png"), 64, 35, 0x10 | 1);
            }
            catch(Exception exception5) { }
            g.drawImage(imgPl, 68, 115, 20);
            g.drawImage(imgBk, 2, 115, 20);
            System.gc();
        }
    }

    public void draw_text_box(Graphics g, String s)
    {
        g.setColor(44783);
        g.fillRect(0, 42, 128, 38);
        g.setColor(20361);
        g.drawRect(0, 42, 127, 38);
        g.setColor(0);
        g.drawString(s, 9, 46, 20);
        g.drawString("challenged you!!", 4, 60, 20);
    }

    public void draw_text(Graphics g)
    {
        int i = message.length();
        g.setColor(44783);
        g.fillRect(0, 52, 128, 19);
        g.setColor(20361);
        g.drawRect(0, 52, 127, 19);
        g.setColor(0);
        Graphics _tmp = g;
        Graphics _tmp1 = g;
        g.drawString(message, 64, 53, 0x10 | 1);
        message = "";
    }

    public void draw_item(Graphics g)
    {
        if(del == -1)
        {
            for(int i = 0; i < 5; i++)
                if(item_slot[i] != 0)
                    g.drawImage(imgItem[item_slot[i]], 12 * i + 37, 111, 20);

        } else
        {
            g.setColor(0x6a6a6a);
            g.fillRect(del * 12 + 37, 111, 8, 8);
            del = -1;
        }
    }

    public void draw_sp_hyo(Graphics g)
    {
        for(int i = 0; i < e_num; i++)
            if(e_hp[i] >= 0)
            {
                g.setColor(0xff0000);
                g.fillRect(e_x[i] * 5 + 8, e_y[i] * 5 + 5, 3, 15);
                g.setColor(0x93959a);
                g.fillRect(e_x[i] * 5 + 8, e_y[i] * 5 + 5, 3, 15 - (15 * e_hp[i]) / max_e_hp[i]);
                if(ppang <= 51)
                {
                    Graphics _tmp = g;
                    Graphics _tmp1 = g;
                    g.drawImage(imgEffect[0], e_x[i] * 5, e_y[i] * 5 + 5, 0x10 | 1);
                } else
                if(ppang <= 54)
                {
                    Graphics _tmp2 = g;
                    Graphics _tmp3 = g;
                    g.drawImage(imgEffect[1], e_x[i] * 5, e_y[i] * 5 + 5, 0x10 | 1);
                }
            }

        if(e_boss_hp >= 0 && e_boss > 0)
        {
            g.setColor(0xff0000);
            g.fillRect(e_boss_x * 5 + 12, e_boss_y * 5 + 5, 3, 15);
            g.setColor(0x93959a);
            g.fillRect(e_boss_x * 5 + 12, e_boss_y * 5 + 5, 3, 15 - (15 * e_boss_hp) / max_e_boss_hp);
            if(ppang <= 51)
            {
                Graphics _tmp4 = g;
                Graphics _tmp5 = g;
                g.drawImage(imgEffect[0], e_boss_x * 5, e_boss_y * 5 + 5, 0x10 | 1);
            } else
            if(ppang <= 54)
            {
                Graphics _tmp6 = g;
                Graphics _tmp7 = g;
                g.drawImage(imgEffect[1], e_boss_x * 5, e_boss_y * 5 + 6, 0x10 | 1);
            }
        }
        if(ppang != 55)
        {
            ppang++;
        } else
        {
            for(int j = 0; j < e_num; j++)
                if(e_hp[j] > 0)
                {
                    if(special == 2)
                    {
                        e_ppang_time[j] = 65;
                        e_ppang_item[j] = 2;
                    } else
                    if(special == 3)
                    {
                        e_ppang_time[j] = 80;
                        e_ppang_item[j] = 1;
                        e_lv[j] = -e_lv[j];
                    }
                    e_move_dir[j] = 0;
                }

            ppang = 0;
            special = 0;
        }
    }

    public void draw_enemy(Graphics g)
    {
        for(int i = 0; i < e_num; i++)
            if(e_x[i] != -10)
            {
                Graphics _tmp = g;
                Graphics _tmp1 = g;
                g.drawImage(imgEnemy[e_idx[i]], e_x[i] * 5, e_y[i] * 5 + 5, 0x10 | 1);
                if(e_ppang_time[i] > 0)
                {
                    e_ppang_time[i]--;
                    Graphics _tmp2 = g;
                    Graphics _tmp3 = g;
                    g.drawImage(imgItem_hyo[e_ppang_item[i] - 1], e_x[i] * 5, e_y[i] * 5 + 1, 0x10 | 1);
                    if(e_ppang_time[i] == 0)
                    {
                        e_ppang_item[i] = 0;
                        if(e_lv[i] < 0)
                            e_lv[i] = -e_lv[i];
                    }
                }
                if(dis_count[i] >= 1)
                {
                    dis_count[i]++;
                    if(dis_count[i] == 4)
                    {
                        dis_count[i] = 0;
                        e_idx[i] = 0;
                    }
                } else
                if(dis_count[i] <= -1)
                {
                    dis_count[i]--;
                    if(dis_count[i] == -10)
                    {
                        e_x[i] = -10;
                        dis_count[i] = 0;
                        e_t_num--;
                        if(e_t_num == 0 && e_boss == 0)
                        {
                            item_mode = 0;
                            game_state = 2;
                            state = 3;
                            gameOn = true;
                        }
                    }
                }
            }

        if(e_boss > 0)
        {
            Graphics _tmp4 = g;
            Graphics _tmp5 = g;
            g.drawImage(imgBoss[e_boss_idx], e_boss_x * 5, e_boss_y * 5, 0x10 | 1);
            if(boss_dis_count >= 1)
            {
                boss_dis_count++;
                if(boss_dis_count == 4)
                {
                    boss_dis_count = 0;
                    e_boss_idx = 0;
                }
            } else
            if(boss_dis_count <= -1)
            {
                boss_dis_count--;
                if(boss_dis_count == -10)
                {
                    e_boss = 0;
                    boss_dis_count = 0;
                    if(e_t_num == 0)
                    {
                        item_mode = 0;
                        game_state = 2;
                        state = 3;
                        gameOn = true;
                    }
                }
            }
        }
    }

    public void draw_gauge(Graphics g)
    {
        if(d_gauge == 2)
        {
            g.setColor(0xfff799);
            g.fillRect(118, 111, 8, 8);
            if(wp != 0)
            {
                Graphics _tmp = g;
                Graphics _tmp1 = g;
                g.drawImage(imgItem[wp], 122, 111, 0x10 | 1);
            }
        }
        if(mana != 0)
        {
            g.setColor(0xff0000);
            g.fillRect(30, 124, mana, 1);
            if(mana == 36)
            {
                g.fillRect(39, 123, 3, 3);
                g.fillRect(51, 123, 3, 3);
                g.fillRect(63, 123, 3, 3);
            } else
            if(mana >= 24)
            {
                g.fillRect(39, 123, 3, 3);
                g.fillRect(51, 123, 3, 3);
            } else
            if(mana >= 12)
                g.fillRect(39, 123, 3, 3);
        } else
        if(mana == 0)
        {
            g.setColor(0x4bb2d9);
            g.fillRect(30, 124, 36, 1);
            g.fillRect(39, 123, 3, 3);
            g.fillRect(51, 123, 3, 3);
            g.fillRect(63, 123, 3, 3);
        }
        d_gauge = 0;
    }

    public void draw_int(Graphics g, int i, int j, int k)
    {
        byte byte0 = 0;
        if(i / 100 > 0)
            byte0 = 3;
        else
        if(i / 10 > 0)
            byte0 = 2;
        else
            byte0 = 1;
        int ai[] = new int[byte0];
        if(byte0 == 3)
        {
            ai[2] = i / 100;
            ai[1] = (i / 10) % 10;
            ai[0] = i % 10;
        } else
        if(byte0 == 2)
        {
            ai[1] = i / 10;
            ai[0] = i % 10;
        } else
        if(byte0 == 1)
            ai[0] = i;
        for(int l = byte0; l > 0; l--)
            if(i < 10)
                g.drawImage(imgNum[ai[l - 1]], (byte0 - l) * 4 + j, k, 20);
            else
                g.drawImage(imgNum[ai[l - 1]], ((byte0 - l) * 4 + j) - 2, k, 20);

    }

    public void run()
    {
        do
            if(gameOn)
            {
                if(screen == 6)
                {
                    if(state == 1)
                    {
                        try
                        {
                            Thread.sleep(game_speed);
                        }
                        catch(Exception exception) { }
                        if(pw_up == 1)
                        {
                            setPower();
                            if(h_idx == 2)
                                h_idx = 3;
                            else
                            if(h_idx == 3)
                                h_idx = 2;
                        } else
                        if(pw_up == 2)
                        {
                            if(h_timer < 4)
                            {
                                h_timer++;
                                if(h_timer == 4)
                                    h_idx = 0;
                            }
                            if(snow_y > snow_last_y)
                            {
                                snow_y--;
                                if(snow_y > snow_top_y)
                                    snow_gap += 3;
                                else
                                if(snow_y < snow_top_y)
                                    snow_gap -= 3;
                            } else
                            {
                                check_ppang();
                            }
                        }
                        e_time++;
                        for(int i = 0; i < e_num; i++)
                        {
                            if(e_hp[i] >= 0)
                            {
                                if(e_time == e_fire_time[i] && get_random(3) != 1 && e_ppang_item[i] != 2)
                                    e_attack_ai(i);
                                if(e_ppang_item[i] != 2)
                                    if(e_idx[i] == 0)
                                        e_idx[i] = 1;
                                    else
                                    if(e_idx[i] == 1)
                                        e_idx[i] = 0;
                            }
                            if(e_move_dir[i] >= 100)
                            {
                                e_move_dir[i]++;
                                if(e_move_dir[i] == 120)
                                    e_move_dir[i] = 0;
                            } else
                            if(e_move_dir[i] == 0 && e_hp[i] > 0 && e_ppang_item[i] != 2)
                                e_move_ai(i);
                            else
                            if(e_move_dir[i] < 100 && e_move_dir[i] != 0 && e_hp[i] > 0)
                                e_move(i);
                        }

                        if(e_boss > 0)
                        {
                            if(e_boss_hp >= 0)
                            {
                                if(e_time == e_boss_fire_time && get_random(3) != 1)
                                    if(e_boss == 1 || e_boss == 2)
                                        e_attack_ai(101);
                                    else
                                        e_attack_ai(102);
                                if(e_boss_idx == 0)
                                    e_boss_idx = 1;
                                else
                                if(e_boss_idx == 1)
                                    e_boss_idx = 0;
                            }
                            if(e_boss_move_dir >= 100)
                            {
                                e_boss_move_dir++;
                                if(e_boss_move_dir == 115)
                                    e_boss_move_dir = 0;
                            } else
                            if(e_boss_move_dir == 0 && e_boss_hp > 0)
                                boss_move_ai();
                            else
                            if(e_boss_move_dir != 0 && e_boss_hp > 0)
                                boss_move();
                        }
                        if(e_num == 3 || e_num == 4)
                        {
                            if(e_time == 21)
                                e_time = 0;
                        } else
                        if(e_num == 2 && e_time == 18)
                            e_time = 0;
                        e_snow();
                        if(gameOn)
                        {
                            repaint();
                            serviceRepaints();
                        }
                    } else
                    if(state == 2)
                    {
                        if(ani_step >= 1 && ani_step <= 20)
                            ani_step++;
                        if(ani_step == 0)
                        {
                            loadImage(-6);
                            ani_step = 1;
                        } else
                        if(ani_step >= 1 && ani_step <= 19)
                        {
                            repaint();
                            serviceRepaints();
                        } else
                        if(ani_step == 20)
                        {
                            destroyImage(-6);
                            state = 1;
                        }
                    } else
                    if(state == 3)
                        if(game_state == 2)
                        {
                            screen = 201;
                            MPlay(7);
                            gold = school * 6 + get_random(7) + 5;
                        } else
                        if(game_state == 1)
                        {
                            screen = -200;
                            gold = 3;
                        }
                } else
                if(screen == 8)
                {
                    if(ani_step < 50 && ani_step > 0)
                        ani_step++;
                    repaint();
                    serviceRepaints();
                } else
                if(screen == 9)
                {
                    if(ani_step < 46 && ani_step >= 0)
                        ani_step++;
                    repaint();
                    serviceRepaints();
                } else
                if(screen == 200)
                {
                    if(ani_step < 51 && ani_step >= 0)
                    {
                        ani_step++;
                        repaint();
                        serviceRepaints();
                    } else
                    {
                        gameOn = false;
                        destroyImage(200);
                        System.gc();
                        if(state != 10)
                        {
                            loadImage(2);
                            screen = 300;
                        } else
                        {
                            screen = 1000;
                        }
                        repaint();
                    }
                } else
                if(screen == 201)
                {
                    ani_step = 0;
                    if(last_stage / 10 == school)
                    {
                        if(stage % 10 != 4)
                            stage++;
                        else
                        if(stage != 44)
                        {
                            stage += 10;
                            stage = (stage - stage % 10) + 1;
                        } else
                        {
                            stage = 45;
                            state = 10;
                        }
                        last_stage = stage;
                    }
                    destroyImage(6);
                    destroyImage(7);
                    destroyImage(100);
                    loadImage(200);
                    screen = 200;
                } else
                if(screen == -200)
                {
                    item_mode = 0;
                    ani_step = 0;
                    destroyImage(6);
                    destroyImage(7);
                    destroyImage(100);
                    loadImage(-200);
                    MPlay(6);
                    screen = -201;
                } else
                if(screen == -201)
                    if(ani_step <= 100)
                    {
                        ani_step++;
                        repaint();
                        serviceRepaints();
                    } else
                    {
                        gameOn = false;
                        destroyImage(-200);
                        loadImage(2);
                        System.gc();
                        screen = 300;
                        repaint();
                    }
            } else
            {
                try
                {
                    Thread.sleep(100L);
                }
                catch(Exception exception1) { }
            }
        while(true);
    }

    public void setPower()
    {
        if(snow_pw < 22)
        {
            snow_pw++;
            real_snow_pw = snow_pw / 3;
        }
    }

    public void make_attack() {
        snow_y = 12;
        snow_x = h_x;
        snow_last_y = 9 - real_snow_pw;
        if(real_snow_pw % 2 == 0)
            snow_top_y = 10 - real_snow_pw / 2;
        else
            snow_top_y = 9 - real_snow_pw / 2;
        snow_gap = 3;
        h_timer = 0;
        pw_up = 2;
        MPlay(2);
    }

    public int get_random(int i) {
        int j = rnd.nextInt() % i;
        if(j < 0)
            j = -j;
        return j;
    }

    public int get_random1(int i) {
        int j = rnd.nextInt() % i;
        if(j == 0)
            j = -5;
        return j;
    }

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

    public void e_snow() {
        for(int i = 0; i < e_num; i++)
            if(e_behv[i] != 100) {
                e_snow_y[i]++;
                e_snow_x[i] = e_snow_x[i] + e_snow_dx[i];
                if(e_snow_gap[i] < 10 && e_snow_top[i] == 1)
                {
                    e_snow_gap[i] += 2;
                    if(e_snow_gap[i] == 10)
                        e_snow_top[i] = 2;
                } else {
                    e_snow_gap[i]--;
                }
                if(e_snow_y[i] == 13)
                    check_hero(e_snow_x[i], i);
                else
                if(e_snow_y[i] >= 16)
                    e_behv[i] = 100;
            }

        if(e_boss > 0 && e_boss_behv != 100) {
            e_boss_snow_y++;
            e_boss_snow_x = e_boss_snow_x + e_boss_snow_dx;
            if(e_boss_snow_gap < 10 && e_boss_snow_top == 1) {
                e_boss_snow_gap += 2;
                if(e_boss_snow_gap == 10)
                    e_boss_snow_top = 2;
            } else {
                e_boss_snow_gap--;
            }
            if(e_boss_snow_y == 13)
                check_hero(e_boss_snow_x, 100);
            else
            if(e_boss_snow_y >= 16)
                e_boss_behv = 100;
        }
    }

    public void check_hero(int i, int j) {
        byte byte0 = 0;
        byte byte1;
        if(j != 100) {
            if(e_behv[j] <= 0) {
                byte0 = 5;
                byte1 = 9;
            } else {
                byte0 = 9;
                byte1 = 5;
            }
        } else if(e_boss_behv <= 0) {
            byte0 = 5;
            byte1 = 9;
        } else {
            byte0 = 9;
            byte1 = 5;
        }
        if(i - h_x * 5 <= byte1 && i - h_x * 5 >= -byte0) {
            int k = -1;
            if(j != 100) {
                e_behv[j] = 100;
                k = e_wp[j];
            } else {
                e_boss_behv = 100;
                k = e_boss_wp;
            }
            h_timer_p = -1;
            if(hp > 0)
                if(k == 0)
                    hp -= e_dem;
                else
                if(k == 1) {
                    ppang_item = 1;
                    ppang_time = 20;
                    hp -= (e_dem * 2) / 3;
                } else
                if(k == 2) {
                    ppang_item = 1;
                    ppang_time = 20;
                    hp -= e_dem;
                } else if(k == 3) {
                    ppang_item = 2;
                    ppang_time = 20;
                    hp -= (e_dem * 2) / 3;
                }
            MPlay(4);
            call_vib(1);
        }
    }

    public void boss_move() {
        if(e_boss_move_dir >= 1 && e_boss_move_dir < 8) {
            e_boss_move_dir++;
            if(e_boss_move_dir == 8)
                e_boss_move_dir = 100;
        } else if(e_boss_move_dir >= 21 && e_boss_move_dir < 31) {
            e_boss_move_dir++;
            if(e_boss_x != 2 && e_boss_move_dir % 2 == 0)
                e_boss_x--;
            if(e_boss_move_dir == 31)
                e_boss_move_dir = 100;
        } else
        if(e_boss_move_dir > -31 && e_boss_move_dir <= -21) {
            e_boss_move_dir--;
            if(e_boss_x != 22 && e_boss_move_dir % 2 == 0)
                e_boss_x++;
            if(e_boss_move_dir == -31)
                e_boss_move_dir = 100;
        }
    }

    public void boss_move_ai() {
        if(e_boss_x == 2)
            e_boss_move_dir = -21;
        else
        if(e_boss_x == 22) {
            e_boss_move_dir = 21;
        } else {
            int i = get_random(6);
            if(i == 0 || i == 1)
                e_boss_move_dir = 21;
            else
            if(i == 2 || i == 3)
                e_boss_move_dir = -21;
            else
                e_boss_move_dir = 1;
        }
    }

    public void e_move_ai(int i)
    {
        if(e_x[i] == 2 || e_x[1] <= 9 && i == 1) {
            int j = get_random(4);
            if(j == 0 || j == 1)
                e_move_dir[i] = -21;
            else
            if(j == 2)
                e_move_dir[i] = -11;
            else
            if(j == 3)
                e_move_dir[i] = 11;
        } else if(e_x[i] == 22 || e_x[0] >= 14 && i == 0) {
            int k = get_random(4);
            if(k == 0 || k == 1)
                e_move_dir[i] = 21;
            else
            if(k == 2)
                e_move_dir[i] = -11;
            else
            if(k == 3)
                e_move_dir[i] = 11;
        } else if(e_y[i] == 6 || e_y[i] == 7) {
            int l = get_random(4);
            if(l == 1 || l == 2)
                e_move_dir[i] = 11;
            else
            if(l == 0)
                e_move_dir[i] = 21;
            else
            if(l == 1)
                e_move_dir[i] = -21;
        } else {
            int i1 = get_random(8);
            if(i1 == 0 || i1 == 1)
                e_move_dir[i] = 21;
            else
            if(i1 == 2 || i1 == 3)
                e_move_dir[i] = -21;
            else
            if(i1 == 4)
                e_move_dir[i] = 11;
            else
            if(i1 == 5)
                e_move_dir[i] = -11;
            else
                e_move_dir[i] = 1;
        }
    }

    public void e_move(int i) {
        int j = e_move_dir[i];
        if(j >= 1 && j < 8) {
            if(++j == 8)
                j = 100;
        } else
        if(j >= 21 && j < 31) {
            j++;
            if(e_x[i] != 2 && j % 3 == 0)
                e_x[i]--;
            if(j == 31)
                j = 100;
        } else if(j > -31 && j <= -21) {
            j--;
            if(e_x[i] != 22 && j % 3 == 0)
                e_x[i]++;
            if(j == -31)
                j = 100;
        } else
        if(j >= 11 && j < 14) {
            j++;
            if(e_y[i] != 1 && j % 2 == 0)
                e_y[i]--;
            if(j == 14)
                j = 100;
        } else
        if(j > -14 && j <= -11) {
            j--;
            if(e_y[i] != 7 && j % 2 == 0)
                e_y[i]++;
            if(j == -14)
                j = 100;
        }
        e_move_dir[i] = j;
    }

    public void use_special() {
        screen = 8;
        ani_step = 1;
        real_snow_pw = 0;
        snow_pw = 0;
        h_idx = 0;
        gameOn = false;
        destroyImage(100);
        loadImage(8);
        if(mana == 36) {
            special = 3;
            dem = 24;
        } else
        if(mana >= 24) {
            special = 2;
            dem = 12;
        } else
        if(mana >= 12) {
            special = 1;
            dem = 12;
        }
        d_gauge = 1;
        MPlay(5);
        call_vib(3);
    }

    public void decs_e_hp(int i) {
        hit_idx = i;
        if(mana != 36)
            if(mana <= 10)
                mana += 2;
            else
                mana++;
        if(i != 10) {
            if(wp == 0)
                e_hp[i] -= dem;
            else if(wp == 1) {
                e_ppang_time[i] = 70;
                e_ppang_item[i] = 1;
                e_lv[i] = -e_lv[i];
                e_hp[i] -= dem;
            } else if(wp == 2) {
                s_item = -10;
                e_hp[i] -= 19;
            } else if(wp == 3) {
                e_ppang_time[i] = 65;
                e_ppang_item[i] = 2;
                e_hp[i] -= dem / 2;
                e_move_dir[i] = 0;
            } else if(wp == 4) {
                e_ppang_time[i] = 75;
                e_ppang_item[i] = 1;
                e_lv[i] = -e_lv[i];
                s_item = -10;
                e_hp[i] -= dem * 2;
            }
            if(e_hp[i] < 0) {
                e_idx[i] = 2;
                dis_count[i] = -1;
            }
        } else if(i == 10) {
            if(wp == 4) {
                s_item = -10;
                e_boss_hp -= dem * 2;
            } else if(wp == 2) {
                s_item = -10;
                e_boss_hp -= 19;
            } else {
                e_boss_hp -= dem;
            }
            if(e_boss_hp < 0) {
                e_boss_idx = 2;
                boss_dis_count = -1;
            }
            if(al == 1)
                e_boss_hp -= 5;
        }
        MPlay(3);
    }

    public void check_ppang() {
        d_gauge = 2;
        for(int i = 0; i < e_num; i++)
        {
            int j = e_x[i];
            if(j - snow_x >= -1 && j - snow_x <= 1)
            {
                int l = e_y[i];
                if(l >= 0 && l <= 4)
                {
                    if(l + real_snow_pw == 7 || l + real_snow_pw == 8)
                    {
                        ppang = 1;
                        decs_e_hp(i);
                        break;
                    }
                } else
                if(l + real_snow_pw == 8 || l + real_snow_pw == 9)
                {
                    ppang = 1;
                    decs_e_hp(i);
                    break;
                }
            }
            ppang = -1;
        }

        if(e_boss > 0 && e_boss_x - snow_x >= -1 && e_boss_x - snow_x <= 1)
        {
            int k = (e_boss_y + real_snow_pw) - 1;
            if(k == 9 || k == 7 || k == 8)
            {
                if(k == 7)
                    al = 1;
                ppang = 1;
                decs_e_hp(10);
            }
        }
        pw_up = -1;
        if(wp != 0)
            wp = 0;
    }

    public void startThread() {
        if(thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void goto_menu() {
        destroyImage(6);
        destroyImage(7);
        destroyImage(100);
        loadImage(2);
        m_mode = 1;
        screen = 2;
    }

    public void check_building(int i, int j) {
        if(i == 43 && j == 22)
        {
            m_mode = 0;
            b_item = 0;
            s_item = 0;
        } else
        if(i == 71 && j == 22)
        {
            m_mode = 1;
            b_item = 0;
            s_item = 0;
        } else
        if(i == 92 && j == 46)
        {
            m_mode = 2;
            school = 1;
        } else
        if(i == 71 && j == 70)
        {
            if(last_stage > 20)
                m_mode = 3;
            else
                m_mode = 100;
            school = 2;
        } else
        if(i == 43 && j == 70)
        {
            if(last_stage > 30)
                m_mode = 4;
            else
                m_mode = 100;
            school = 3;
        } else
        if(i == 22 && j == 46)
        {
            if(last_stage > 40)
                m_mode = 5;
            else
                m_mode = 100;
            school = 4;
        } else
        {
            m_mode = -1;
        }
    }

    public int hero_move(int i, int j, int k) {
        if(k == 0)
            if(j == 46 && i >= 22 && i <= 92)
            {
                check_building(i, j);
                return 1;
            } else
            {
                return 0;
            }
        if(k == 1)
        {
            if((i == 43 || i == 71) && j >= 15 && j <= 71)
            {
                check_building(i, j);
                return 1;
            } else
            {
                return 0;
            }
        } else
        {
            return 1;
        }
    }

    public int input_item(int i) {
        for(int j = 0; j < 5; j++)
            if(item_slot[j] == i && i <= 8)
                return 3;

        for(int k = 0; k < 5; k++)
            if(item_slot[k] == 0)
            {
                item_slot[k] = i;
                if(i == 1)
                    item_a_num = 2;
                else
                if(i == 2)
                    item_b_num = 2;
                else
                if(i == 3)
                    item_c_num = 2;
                else
                if(i == 4)
                    item_d_num = 2;
                return 1;
            }

        return 0;
    }

    public void use_item(int i) {
        if(item_slot[i] > 0 && item_slot[i] <= 4)
        {
            wp = item_slot[i];
            if(wp == 1)
            {
                item_a_num--;
                if(item_a_num == 0)
                    delete_item(1);
            } else
            if(wp == 2)
            {
                item_b_num--;
                if(item_b_num == 0)
                    delete_item(2);
            } else
            if(wp == 3)
            {
                item_c_num--;
                if(item_c_num == 0)
                    delete_item(3);
            } else
            if(wp == 4)
            {
                item_d_num--;
                if(item_d_num == 0)
                    delete_item(4);
            }
            d_gauge = 2;
        } else
        if(item_slot[i] == 5)
        {
            delete_item(5);
            hp += max_hp / 3;
            if(hp > max_hp)
                hp = max_hp;
            h_timer_p = -4;
        } else
        if(item_slot[i] == 6)
        {
            delete_item(6);
            mana += 10;
            if(mana > 36)
                mana = 36;
            d_gauge = 1;
        } else
        if(item_slot[i] == 7)
        {
            delete_item(7);
            hp = max_hp;
            h_timer_p = -4;
        } else
        if(item_slot[i] == 8)
        {
            delete_item(8);
            hp += max_hp / 3;
            if(hp > max_hp)
                hp = max_hp;
            h_timer_p = -4;
            ppang_item = 0;
            ppang_time = 0;
        }
        item_mode = 100;
        MPlay(1);
    }

    public void delete_item(int i) {
        for(int j = 0; j < 5; j++)
            if(item_slot[j] == i)
            {
                item_slot[j] = 0;
                del = j;
                return;
            }

    }

    public void keyPressed(int i) {
        if(screen == 6 && state == 1)
        {
            if(getGameAction(i) == 2 || i == 52)
            {
                if(item_mode == 0 && ppang_item != 2)
                {
                    if(h_x != 2)
                    {
                        h_x--;
                        if(h_idx == 0)
                            h_idx = 1;
                        else
                        if(h_idx == 1)
                            h_idx = 0;
                    }
                } else
                if(item_mode != 0)
                {
                    if(item_mode != 1)
                        item_mode--;
                    message = "Item Mode";
                    repaint();
                }
            } else
            if(getGameAction(i) == 5 || i == 54)
            {
                if(item_mode == 0 && ppang_item != 2)
                {
                    if(h_x != 23)
                    {
                        h_x++;
                        if(h_idx == 0)
                            h_idx = 1;
                        else
                        if(h_idx == 1)
                            h_idx = 0;
                    }
                } else
                if(item_mode != 0)
                {
                    if(item_mode != 5)
                        item_mode++;
                    message = "Item Mode";
                    repaint();
                }
            } else
            if(getGameAction(i) == 6 || i == 56)
            {
                if(mana >= 12)
                    use_special();
                else
                    message = "Insufficient Mana";
            } else
            if(i == -5 || getGameAction(i) == 1 || i == 50 || i == 53)
            {
                if(item_mode == 0)
                {
                    if(pw_up == 0 && ppang_item != 2)
                    {
                        snow_pw = 0;
                        real_snow_pw = 0;
                        pw_up = 1;
                        h_idx = 2;
                    } else
                    if(pw_up == 1 && real_snow_pw > 0)
                    {
                        h_idx = 4;
                        make_attack();
                    }
                } else
                {
                    use_item(item_mode - 1);
                    gameOn = true;
                }
            } else
            if((i == 35 || i == -7) && game_state == 0)
            {
                m_mode = 1;
                gameOn = false;
                screen = 100;
                repaint();
            } else
            if(i == 51 && game_state == 0)
            {
                int j = 0;
                boolean flag = false;
                for(; j < 5; j++)
                {
                    if(item_slot[j] == 0)
                        continue;
                    flag = true;
                    break;
                }

                if(flag)
                {
                    gameOn = false;
                    message = "Item Mode";
                    item_mode = 1;
                    repaint();
                } else
                {
                    message = "No Item";
                }
            }
        } else
        if(screen == 100) {
            if(getGameAction(i) == 1) {
                if(m_mode == 1)
                    m_mode = 5;
                else
                    m_mode--;
            } else
            if(getGameAction(i) == 6) {
                if(m_mode == 5)
                    m_mode = 1;
                else
                    m_mode++;
            } else
            if(getGameAction(i) == 2) {
                if(m_mode == 3)
                    s_play = 1;
            } else if(getGameAction(i) == 5) {
                if(m_mode == 3)
                    s_play = 2;
            } else
            if(i == 35 || getGameAction(i) == 8 || i == -7)
                if(m_mode == 2)
                {
                    goto_menu();
                } else
                {
                    if(m_mode == 5)
                    {
                        SJ.destroyApp(true);
                        SJ.notifyDestroyed();
                        return;
                    }
                    if(m_mode == 1)
                    {
                        screen = 6;
                        if(item_mode == 0)
                        {
                            gameOn = true;
                        } else
                        {
                            message = "Item Mode";
                            repaint();
                        }
                    } else
                    if(m_mode == 4)
                        screen = -5;
                }
            repaint();
        } else if(screen == 2) {
            if(getGameAction(i) == 1)
            {
                if(m_mode <= 1)
                    m_mode = 4;
                else
                    m_mode--;
            } else
            if(getGameAction(i) == 6)
            {
                if(m_mode >= 4)
                    m_mode = 1;
                else
                    m_mode++;
            } else
            if(i == 35 || getGameAction(i) == 8 || i >= 49 && i <= 52 || i == -7) {
                if(i > 48)
                    m_mode = i - 48;
                if(m_mode == 1)
                    screen = -88;
                if(m_mode == 3)
                {
                    screen = 4;
                    m_mode = 1;
                }
                if(m_mode == 2)
                {
                    m_mode = 1;
                    screen = 5;
                }
                if(m_mode == 4)
                {
                    SJ.destroyApp(true);
                    SJ.notifyDestroyed();
                    return;
                }
            }
            repaint();
        } else if(screen == 3) {
            if(getGameAction(i) == 1)
            {
                int i1 = h_y - 8;
                if(hero_move(h_x, i1, 1) > 0)
                    h_y = i1;
            } else
            if(getGameAction(i) == 6)
            {
                int j1 = h_y + 8;
                if(hero_move(h_x, j1, 1) > 0)
                    h_y = j1;
            } else
            if(getGameAction(i) == 5)
            {
                int k = h_x + 7;
                if(hero_move(k, h_y, 0) > 0)
                    h_x = k;
            } else
            if(getGameAction(i) == 2)
            {
                int l = h_x - 7;
                if(hero_move(l, h_y, 0) > 0)
                    h_x = l;
            } else
            if(i == 35 || getGameAction(i) == 8 || i == -7)
            {
                if(m_mode == 0 || m_mode == 1)
                {
                    loadImage(31);
                    screen = 31;
                    repaint();
                } else
                if(m_mode >= 2 && m_mode <= 5)
                {
                    int l1 = -1;
                    if(last_stage / 10 - school == 0 && last_stage != 45)
                        l1 = last_stage;
                    destroyImage(3);
                    message = "Loading";
                    init_game(l1);
                }
            } else
            if(i == 42 || i == -6)
            {
                destroyImage(3);
                loadImage(2);
                screen = 2;
                m_mode = 1;
            }
            repaint();
        } else
        if(screen == 31) {
            if(getGameAction(i) == 1)
                s_item = 0;
            if(getGameAction(i) == 6)
                s_item = 1;
            if(getGameAction(i) == 2) {
                if(b_item != 0)
                    b_item--;
            } else
            if(getGameAction(i) == 5 && b_item != 3)
                b_item++;
            if(i == 35 || getGameAction(i) == 8 || i == -7)
                if(s_item == 1)
                {
                    m_mode = -1;
                    destroyImage(31);
                    screen = 3;
                } else
                if(s_item == 0) {
                    byte byte0 = 0;
                    if(m_mode == 0)
                        byte0 = 4;
                    if(saved_gold >= item_price[b_item + byte0])
                    {
                        int k1 = input_item(b_item + byte0 + 1);
                        if(k1 == 1)
                        {
                            saved_gold -= item_price[b_item + byte0];
                            message = "Purchasing Items";
                        } else
                        if(k1 == 0)
                            message = "Bag is full";
                        else
                        if(k1 == 3)
                            message = "Duplicated item";
                    } else {
                        message = "not enough gold";
                    }
                }
            repaint();
        } else if(screen == 4) {
            if(i == 42 || i == -6) {
                screen = 2;
                if(speed == 5)
                    game_speed = 8;
                if(speed == 4)
                    game_speed = 17;
                if(speed == 3)
                    game_speed = 24;
                if(speed == 2)
                    game_speed = 31;
                if(speed == 1)
                    game_speed = 38;
                addScore("config", 1);
                m_mode = 3;
            }
            if(getGameAction(i) == 1)
                if(m_mode == 1)
                    m_mode = 3;
                else
                    m_mode--;
            if(getGameAction(i) == 6)
                if(m_mode == 3)
                    m_mode = 1;
                else
                    m_mode++;
            if(m_mode == 1)
            {
                if(getGameAction(i) == 2)
                    s_play = 1;
                if(getGameAction(i) == 5)
                    s_play = 2;
            }
            if(m_mode == 2) {
                if(getGameAction(i) == 2)
                    v_mode = 1;
                if(getGameAction(i) == 5)
                    v_mode = 2;
            }
            if(m_mode == 3) {
                if(getGameAction(i) == 2 && speed != 1)
                    speed--;
                if(getGameAction(i) == 5 && speed != 5)
                    speed++;
            }
            repaint();
        } else
        if(screen == 5) {
            if(getGameAction(i) == 1)
                if(m_mode == 1)
                    m_mode = 3;
                else
                    m_mode--;
            if(getGameAction(i) == 6)
                if(m_mode == 3)
                    m_mode = 1;
                else
                    m_mode++;
            if(i == 35 || getGameAction(i) == 8 || i == 49 || i == 50 || i == 51 || i == -7) {
                if(i > 48)
                    m_mode = i - 48;
                if(m_mode == 1)
                    screen = -33;
                if(m_mode == 2)
                    screen = -33;
                if(m_mode == 3)
                    screen = -33;
            }
            if(i == 42 || i == -6)
            {
                screen = 2;
                m_mode = 2;
            }
            repaint();
        } else
        if(screen == -5) {
            screen = 100;
            repaint();
        } else
        if(screen == -88) {
            if(getGameAction(i) == 1)
                if(m_mode <= 1)
                    m_mode = 2;
                else
                    m_mode--;
            if(getGameAction(i) == 6)
                if(m_mode >= 2)
                    m_mode = 1;
                else
                    m_mode++;
            if(i == 35 || getGameAction(i) == 8 || i == 49 || i == 50 || i == -7) {
                if(i > 48)
                    m_mode = i - 48;
                if(m_mode == 1)
                {
                    last_stage = 11;
                    stage = 11;
                    saved_gold = 0;
                    mana = 0;
                    addScore("hero", 0);
                }
                destroyImage(2);
                loadImage(3);
                h_x = 57;
                h_y = 46;
                m_mode = -1;
                screen = 3;
            }
            if(i == 42 || i == -6)
            {
                screen = 2;
                m_mode = 1;
            }
            repaint();
        } else
        if(screen == -33) {
            if(i == 42 || i == -6)
            {
                loadImage(2);
                m_mode = 1;
                screen = 5;
                repaint();
                System.gc();
            }
        } else
        if(screen == 300)
        {
            MPlay(3);
            m_mode = -1;
            destroyImage(2);
            loadImage(3);
            h_x = 57;
            h_y = 46;
            saved_gold += gold;
            addScore("hero", 0);
            ani_step = 0;
            screen = 3;
            repaint();
            System.gc();
        } else
        if(screen == -1)
        {
            loadImage(-2);
            screen = -2;
            repaint();
        } else
        if(screen == -2)
        {
            loadImage(2);
            screen = 1;
            repaint();
        } else
        if(screen == 1)
        {
            if(i == 35 || i == -7)
            {
                screen = -88;
                m_mode = 1;
            } else
            if(i == 42 || i == -6)
                screen = 2;
            repaint();
        } else
        if(screen == 1000)
        {
            loadImage(2);
            System.gc();
            screen = 300;
            repaint();
        }
    }

    public void MPlay(int i) {
        switch(i) {
            case 0: playSound('0'); break;
            // ...
        }
    }

    public void call_vib(int i) {
        if(v_mode == 1)
            Vibration.start(i, 3);
    }

    public void showNotify() {
        p_mode = 1;
        d_gauge = 2;
    }

    private static final int DEFAULT_DEM = 12;
    RecordStore recordStore;
    private int game_state;
    private int saved_gold;
    private int speed;
    private int game_speed;
    private Random rnd;
    private SnowBallFight SJ;
    private Thread thread;
    private int screen;
    private boolean gameOn;
    private String message;
    private int m_mode;
    private int s_play;
    private int v_mode;
    AudioClip audioClip;
    private int p_mode;
    private int ani_step;
    private Image imgLogo;
    private Image imgMM;
    private Image imgBk;
    private Image imgSl;
    private Image imgPl;
    private Image imgCh;
    private Image imgNum[];
    private Image imgBack;
    private Image imgHero[];
    private Image imgEnemy[];
    private Image imgBoss[];
    private Image imgAl;
    private Image imgShadow;
    private Image imgPok;
    private Image imgPPang;
    private Image imgPPang1;
    private Image imgH_ppang;
    private Image imgSnow_g;
    private Image imgPwd;
    private Image imgItem[];
    private Image imgItem_hyo[];
    private Image imgVill;
    private Image imgSchool;
    private Image imgShop;
    private Image imgSpecial[];
    private Image imgSp;
    private Image imgEffect[];
    private Image imgVictory;
    private Image imgV;
    private Image imgHero_v;
    private Image imgLose;
    private Image imgHero_l;
    private Image imgStage_num;
    private Image imgStage[];
    private int stage;
    private int last_stage;
    private int tmp_stage;
    private int school;
    private int state;
    private int h_x;
    private int h_y;
    private int h_idx;
    private int h_timer;
    private int h_timer_p;
    private int pw_up;
    private int mana;
    private int hp;
    private int max_hp;
    private int dem;
    private int wp;
    private int snow_pw;
    private int real_snow_pw;
    private int snow_last_y;
    private int snow_top_y;
    private int snow_gap;
    private int snow_y;
    private int snow_x;
    private int ppang;
    private int gold;
    private int item_slot[];
    private int ppang_item;
    private int ppang_time;
    private int special;
    private int item_mode;
    private int item_price[];
    private int del;
    private int item_a_num;
    private int item_b_num;
    private int item_c_num;
    private int item_d_num;
    private int b_item;
    private int s_item;
    private int e_x[];
    private int e_y[];
    private int e_num;
    private int e_t_num;
    private int e_idx[];
    private int e_hp[];
    private int max_e_hp[];
    private int e_snow_y[];
    private int e_snow_x[];
    private int e_snow_top[];
    private int e_snow_gap[];
    private int e_snow_dx[];
    private int e_ppang_item[];
    private int e_ppang_time[];
    private int e_lv[];
    private int e_fire_time[];
    private int e_move_dir[];
    private int e_time;
    private int e_dem;
    private int e_wp[];
    private int e_behv[];
    private int hit_idx;
    private int dis_count[];
    private int e_boss;
    private int e_boss_idx;
    private int e_boss_x;
    private int e_boss_y;
    private int e_boss_fire_time;
    private int e_boss_hp;
    private int max_e_boss_hp;
    private int e_boss_snow_y;
    private int e_boss_snow_x;
    private int e_boss_snow_top;
    private int e_boss_snow_gap;
    private int e_boss_snow_dx;
    private int e_boss_wp;
    private int e_boss_behv;
    private int e_boss_move_dir;
    private int boss_dis_count;
    private int al;
    private int d_gauge;
}
