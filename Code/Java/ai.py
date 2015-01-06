from random import randrange
from array import array

e_boss_x = 10
e_boss_y = 6
h_x = 200
e_num = 4 # fix 4 enermy

e_x = array('i',[1,2,3,4])    # array ko fai 1 2 3 4 ma co the la ma 100 cho start_e_x ...
e_y = array('i',[1,2,3,4])

# level cua enermy 
e_lv = array('i',[1,2,3,4])

e_idx = array('i',[1,2,3,4])

#wtf?
e_behv = array('i',[1,2,3,4])
e_snow_y = array('i',[1,2,3,4])
e_snow_x = array('i',[1,2,3,4])
e_snow_gap = array('i',[1,2,3,4])
e_snow_top = array('i',[1,2,3,4])
e_snow_dx = array('i',[1,2,3,4])
e_fire_time = array('i',[1,2,3,4])
e_ppang_item = array('i',[1,2,3,4])
e_ppang_time = array('i',[1,2,3,4])
e_move_dir = array('i',[1,2,3,4])

#
dis_count =  array('i',[1,2,3,4])

e_wp = array('i',[0,1,2,3,4,5, 6, 7, 8]); # e_wp khoi tao la = new int(e_num); tuc so enemy

def e_attack_ai(i):
    j = 0
    k = 0
    l = 0
    i1 = 0
    if i > 100:
        i1 = h_x - e_boss_x
        k = i
    else:
        i1 = h_x - e_x[i]
        l = i

    # i1 wtf
    # It seem simple like basic idea of ai when enermy move toward hero by distance value
    # when hero move, based on distance to enermy, --> e move n fire follow him

    # Viec truyen i 102 101 va ham attack_ai ko cover het th khi runtime vi con nhieu argument khac 
    # cung thay doi khi enermy di chuyen, nem bong ...
    if (i1 >= -9) and (i1 <= -6):
        if e_lv[l] >= 2 or k > 100:
            j1 = get_random(3)
            if j1 == 0:
                j = -2
            elif j1 == 1:
                j = -3
            else:
                j = -4
        else:
            j = get_random1(6)

    elif(i1 >= -5 and i1 <= -2):
        if(e_lv[l] >= 2 or k > 100):
            k1 = get_random(3)
            if(k1 == 0):
                j = -1
            elif(k1 == 1):
                j = -2
            else:
                j = -3
        else:
            j = get_random1(6)
        
    elif(i1 >= -1 and i1 <= 1) :
        if(e_lv[l] >= 2 or k > 100) :
            l1 = get_random(3) 
            if(l1 == 0):
                j = -1
            elif(l1 == 1):
                j = 0
            else:
                j = 1
        else :
            j = get_random1(6)
    elif(i1 >= 2 and i1 <= 5) :
        if(e_lv[l] >= 2 or k > 100) :
            i2 = get_random(3)
            if(i2 == 0):
                j = 1
            elif(i2 == 1):
                j = 2
            else:
                j = 3
        else:
            j = get_random1(6)
        
    elif(i1 >= 6 and i1 <= 9):
        if(e_lv[l] >= 2 or k > 100):
            j2 = get_random(3)
            if(j2 == 0):
                j = 2
            elif(j2 == 1):
                j = 3
            else:
                j = 4
        else :
            j = get_random1(6)
    elif(i1 >= 10) :
        if(e_lv[l] >= 2 or k > 100):
            k2 = get_random(3)
            if(k2 == 0):
                j = 4
            else:
                j = 5
        else :
            j = get_random1(6)
    elif(i1 <= -10) :
        if(e_lv[l] >= 2 or k > 100) :
            l2 = get_random(2)
            if(l2 == 0):
                j = -4
            else:
                j = -5
        else :
            j = get_random1(6)
    if(k < 100) :
        if(e_lv[l] >= 2) :
            i3 = get_random(4)
            if(i3 == 1):
                i4 = get_random(3)
                if(i4 == 0):
                    e_wp[l] = 1
                elif(i4 == 1):
                    e_wp[l] = 2
                else:
                	e_wp[l] = 3
            else :
                e_wp[l] = 0

        e_behv[l] = j
        e_snow_y[l] = e_y[l]
        e_snow_x[l] = e_x[l] * 5
        e_snow_gap[l] = 0
        e_snow_dx[l] = j
        e_snow_top[l] = 1
        e_idx[l] = 3
        dis_count[l] = 1
    else :
        if(k < 103) :
            j3 = get_random(2)
            if(j3 == 0) :
                k3 = get_random(3)
                if(k3 == 0):
                    e_boss_wp = 1
                elif(k3 == 1):
                    e_boss_wp = 2
                else:
                    e_boss_wp = 3
            else:
                e_boss_wp = 0
        else :
            l3 = get_random(3)
            if(l3 == 0):
                e_boss_wp = 1
            elif(l3 == 1):
                e_boss_wp = 2
            else:
                e_boss_wp = 3

        e_boss_behv = j
        e_boss_snow_y = e_boss_y
        e_boss_snow_x = e_boss_x * 5
        e_boss_snow_gap = 0
        e_boss_snow_dx = j
        e_boss_snow_top = 1
        e_boss_idx = 3
        boss_dis_count = 1

    print j, k, l, i1

# Lay random 1 so int < i ? 
def get_random(i):
	# Random nextInt roi chia lay du cho i ??? tai sao ko random(range) luon ?
    # j = rnd.nextInt() % i 
    j = randrange(i)
    if j < 0:
        j = -j
    return j

def get_random1(i):
    # int j = rnd.nextInt() % i
    j = randrange(i)
    if(j == 0):
        j = -5 #wtf
    return j

print 'j  k  l  i1 : '
# if(e_time == e_fire_time[i] && get_random(3) != 1 && e_ppang_item[i] != 2)
# e_attack_ai(i)
for x in range(101, 103):  # game chi co 2 lan goi e_attack_ai(102|101)
    e_attack_ai(x)


# if(e_move_dir[i] == 0 && e_hp[i] > 0 && e_ppang_item[i] != 2)
#                                e_move_ai(i);
# Doan nay cho thay co the chi khi enemy ko dinh dan effect, hoac nguoc lai no moi move_ai
# e_ppang_item = 2 ro la special item --> bi dinh effect moi move_ai

