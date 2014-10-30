e_boss_x = 100
h_x = 200

def e_attack_ai(i):
        j = 0
        k = 0
        l = 0
        i1 = 0
        if i > 100:
            i1 = h_x - e_boss_x
            k = i
            print i1
        else:
            i1 = h_x - e_x[i]
            l = i
        if i1 >= -9 && i1 <= -6:
            if e_lv[l] >= 2 || k > 100:
                j1 = get_random(3)
                if j1 == 0:
                    j = -2
                else:
                if j1 == 1:
                    j = -3
                else:
                    j = -4
            else:
                j = get_random1(6)

def get_random(i):
    j = rnd.nextInt() % i
    if j < 0:
        j = -j
    return j

e_attack_ai(101)
e_attack_ai(102)