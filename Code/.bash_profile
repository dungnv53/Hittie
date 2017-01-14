export CLICOLOR=1
export LSCOLORS=GxFxCxDxBxegedabagaced
alias ls='ls -GFh'
alias l='ls -GFh'
alias pingggs='ping -n google.com'
alias pinglocals='ping 192.168.0.1'
alias pingnadia='ping 192.168.10.254'
alias ping10='ping 10.0.0.254'
alias ping101='ping 10.0.0.1'
alias w3mgg='w3m google.com'
alias getip='ifconfig |grep inet'

alias dnscache='sudo dscacheutil -flushcache'

alias c='clear'
alias ll='ls -la'
alias l.='ls -d .* --color=auto'
alias c="clear"
alias l.='ls -d .* --color=auto'
alias ..='cd ..'
alias ...='cd ../..'
alias duh='du -sh'
alias dus='du -sh * |sort -n'
alias findname='find . -name '
alias findloc='/usr/local/bin/findloc.sh'
alias grr='grep -r'
alias grl='grep -rl'
alias grn='grep -rn'

#git
alias gitst='git status'
alias gitpom='git push origin master'
alias gitpod='git push origin develop'
alias gitpuom='git pull origin master'
alias gitpuod='git pull origin develop'
alias gitpdungd='git push dungnv develop'
alias gitp='git pull'
alias gitco='git checkout'
alias gitdiff='git diff'
alias gitlog='git log'
alias gitcm='git commit -m '
alias gitcmnr='git log |grep commit |wc -l'
alias gitmerd='git merge develop'

alias gitcofr='/Users/dungnv/bin/merge_arg.sh'
alias gitpufr='/Users/dungnv/bin/pull_front.sh'
alias gitpfr='/Users/dungnv/bin/push_front.sh'

alias gitcofe='/Users/dungnv/bin/chkout_feature.sh'
alias gitpufe='/Users/dungnv/bin/pull_feature.sh'
alias gitpfe='/Users/dungnv/bin/push_feature.sh'
alias gitstapl='git stash apply'

alias cdmama='cd /Applications/MAMP/'

alias sourcebash='source ~/.bash_profile'
alias viprofile='sudo vim ~/.bash_profile'

#alias phpunit='vendor/bin/phpunit'
alias mampsql='/Applications/MAMP/Library/bin/mysql -u root -p'
alias mampctl=' sudo /Applications/MAMP/Library/bin/apachectl restart'
alias vimphost=' vim /Applications/MAMP/conf/apache/extra/httpd-vhosts.conf'

alias tailsql='tail -f /Applications/MAMP/logs/mysql_sql.log'
alias tailsql2='tail -f /Applications/MAMP/logs/mysql_error_log.err'
alias tailphp='tail -f /Applications/MAMP/logs/php_error.log'
alias tailapache='tail -f /Applications/MAMP/logs/apache_error.log'
alias tailemp_er='tail -f /Applications/MAMP/logs/emp_9demo_error_log'
alias tailemp_ac='tail -f /Applications/MAMP/logs/emp_9demo_access.log'
alias tailgc_er='tail -f /Applications/MAMP/logs/gcc_9demo_error_log'
alias tailgc_ac='tail -f /Applications/MAMP/logs/gcc_9demo_access.log'
alias tailjob_er='tail -f /Applications/MAMP/logs/job_9demo_error_log'
alias tailjob_ac='tail -f /Applications/MAMP/logs/job_9demo_access.log'

# Math support
alias bc='bc-l'
alias hist='history'
alias j='jobs -l'


alias svi='sudo vi'
alias vis='vim "+set si"'
alias vinu='vim "+set nu"'

alias ping='ping -c 5'
alias fastping='ping -c 100 -s.2'

# Show opon port
alias ports='netstat -tulanp'
alias phparts='php artisan serve'

## shortcut  for iptables and pass it via sudo#
alias ipt='sudo /sbin/iptables'

# display all rules #
alias iptlist='sudo /sbin/iptables -L -n -v --line-numbers'
alias iptlistin='sudo /sbin/iptables -L INPUT -n -v --line-numbers'
alias iptlistout='sudo /sbin/iptables -L OUTPUT -n -v --line-numbers'
alias iptlistfw='sudo /sbin/iptables -L FORWARD -n -v --line-numbers'
alias firewall=iptlist


# Windows 8 pg
alias c='clear'
alias ll='ls -la'
alias l.='ls -d .* --color=auto'
alias c="clear"
alias l.='ls -d .* --color=auto'
alias ..='cd ..'
alias ...='cd ../..'
alias duh='du -sh'
alias dus='du -sh * |sort -n'
alias findname='find . -name '
alias findloc='/usr/local/bin/findloc.sh'
alias grr='grep -r'
alias grl='grep -rl'
alias grn='grep -rn'
alias grni='grep -rni'

alias vg='vagrant'
alias tailapc='tail -f /d/xampp/apache/logs/error.log'

alias cdxam6='cd /e/setin/xampp_56/htdocs/'
alias vihost='vim /c/Windows/System32/drivers/etc/hosts'
alias cathost='cat /c/Windows/System32/drivers/etc/hosts'
alias vivhost='vim /e/setin/xampp_56/apache/conf/extra/httpd-vhosts.conf'
alias catvhost='cat /e/setin/xampp_56/apache/conf/extra/httpd-vhosts.conf'
alias apcrst='/e/setin/xampp_56/apache/bin/httpd.exe -k restart'
alias cdorg2='cd /e/setin/xampp_56/htdocs/Organizer/ServiceApi/OrganizerApi'
alias cdorg='cd /e/setin/xampp_56/htdocs/Organizer/SourceCodeDemo/ServiceApi/OrganizerApi/'
alias cdorgdemo='cd /e/setin/xampp_56/htdocs/Organizer/SourceCodeDemo'


alias tailsql='tail -f /e/setin/xampp_56/mysql/data/mysql_query.log'

alias hist='history'

alias cdsbf='cd /e/ArchAngel/SnowBallFightLibGdx'

alias sshblondie='ssh -p 3022 blondie@127.0.0.1'

alias convert='/c/Program\ Files/ImageMagick-6.9.6-Q16-HDRI/convert.exe '

alias catacc='cat ~/.COPY/account'

#TODO update ubuntu alias about nginx apc

export PATH="/usr/local/bin:$PATH"
export PATH=/Applications/MAMP/bin/php/php5.4.10/bin:$PATH
[[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm" # Load RVM into a shell session *as a function*

# ulimit for mongo warning added by dungnv 28/9
ulimit -n 2048

##
# Your previous /Users/dungnv/.bash_profile file was backed up as /Users/dungnv/.bash_profile.macports-saved_2014-05-22_at_10:54:28
##

# MacPorts Installer addition on 2014-05-22_at_10:54:28: adding an appropriate PATH variable for use with MacPorts.
export PATH=/opt/local/bin:/opt/local/sbin:$PATH
export PATH="$PATH:/opt/local/bin"
export PATH=/Applications/MAMP/bin/php/php5.5.10/bin:$PATH

export DB_PASSWORD='root'
# Finished adapting your PATH environment variable for use with MacPorts.


HISTFILESIZE=12080
