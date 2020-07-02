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

alias netpentl='sudo netstat -pentl'
alias netln='sudo netstat -putln'

alias dnscache='sudo dscacheutil -flushcache'

alias c='clear'
alias ll='ls -la'
alias ls='ls --color'
alias l='ls --color'
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
alias grni='grep -rni'

# Git
# If one command used frequently and/or it often mis-typed then it should be shortened by the way that make it easier to type, remmeber ... and certainly avoid conflict with existing command.
alias gst='git status'
alias gitpom='git push origin master'
alias gitpuod='git pull origin develop'
alias gitpo='git pull origin '
alias gitp='git pull'
alias gco='git checkout'
alias gdf='git diff'
alias gitlog='git log'
alias gitcm='git commit -m '
alias gitcmnr='git log |grep commit |wc -l'
alias gitfc='git show --pretty="format:" --name-only ' # show commit files change only

alias gitbrt='git br && git st'
alias gitlog='git log --graph --decorate --pretty=oneline --abbrev-commit'

# git config --global alias.co checkout
# git config --global alias.br branch
# git config --global alias.ci commit
# git config --global alias.st status

alias apcrld='sudo service apache2 reload'
alias apcrst='sudo service apache2 restart'

alias ngrst='sudo service nginx restart'
alias ngrld='sudo service nginx reload'
alias fpmrst='sudo service php7.0-fpm restart'

alias sourcebash='source ~/.bash_profile'
alias viprofile='sudo vim ~/.bash_profile'

#alias phpunit='vendor/bin/phpunit'

alias tailsql='tail -f /var/log/mysql/mysql.log'

alias apctest='sudo apachectl configtest'
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


alias w3g='w3m google.com'

alias comandos='cat ~/.COPY/cmd'

alias cnode='clear && nodemon server.js'
alias pmr0='clear && pm2 restart 0'
alias frs='forever start server.js'
alias frl='forever list'
alias cdprj='cd /mnt/PROJECT/Cargo/AVCONV/'
alias synctxpb='cd /mnt/PROJ/rt3/ && rsync -ua --exclude 'node_modules' --exclude '.git' --exclude 'files' /home/cam_zink/Public/PROJ/* .'


# Docker
alias dkrmi='sudo docker rmi '
alias dkrm='sudo docker rm '
alias dkim='sudo docker images'
alias dkstp='sudo docker stop '
alias dkima='sudo docker images -a'
alias dkps='sudo docker ps'
alias dkpsa='sudo docker ps -a'
alias dkinspt='sudo docker inspect '
alias dkprunea='sudo docker system prune -a '
alias dkhist='sudo docker history '
alias dkrename='sudo docker remame '
alias dkbash='sudo docker exec -i -t $1 /bin/bash'
alias dkst='sudo docker stats '
alias dkhlp='cat ~/.COPY/.dockerone'
alias dkvl='sudo docker volume ls '
alias dkvlisp='sudo docker volume inspect '
alias dk='sudo docker '
alias dkhist='sudo docker history '
alias dknetw='sudo docker network ls'
alias dkattach='sudo docker attach '

# Docker Machine
alias dkm='sudo docker-machine '
alias dkmls='sudo docker-machine ls'
alias dkmrst='sudo docker-machine restart '

alias dkvlrm='sudo docker volume rm '

#pm2
alias pml='pm2 list '
alias pms='pm2 show '
alias pmst='pm2 stop '
alias pmsta='pm2 start '
alias pml='pm2 log '

#mount
alias mountprj='sudo mount -t vboxsf PROJECT /mnt/PROJECT/'

# Aptible
alias aptinst='sudo apt-get install -y '


# Search keyword with file extension
findsrc2() {
	if [ -z "$1" ]; then # In case empty file extension, set a default one to avoid other error
		ext="*.sh"
	else
		ext="*."$1
	fi

	echo "$ext"
	keyword=$2
	if [ ${#keyword} -lt 2 ]; then
		echo "${keyword}  have at least 2 character"
		exit 1
	fi

    echo "$2"
    find . -name "${ext}" -print0 | xargs -0 grep -rn "$2"
}
findsrci() {
	if [ -z "$1" ]; then # In case empty file extension, set a default one to avoid other error
		ext="*.sh"
	else
		ext="*."$1
	fi

	echo "$ext"
	keyword=$2
	if [ ${#keyword} -lt 2 ]; then
		echo "${keyword}  have at least 2 character"
		exit 1
	fi

    echo "$2"
    find . -name "${ext}" -print0 | xargs -0 grep -rni "$2"
}
findsrc_l() {
	if [ -z "$1" ]; then # In case empty file extension, set a default one to avoid other error
		ext="*.sh"
	else
		ext="*."$1
	fi

	echo "$ext"
	keyword=$2
	if [ ${#keyword} -lt 2 ]; then
		echo "${keyword}  have at least 2 character"
		exit 1
	fi

    echo "$2"
    find . -name "${ext}" -print0 | xargs -0 grep -rl "$2"
}


export PATH="/usr/local/bin:$PATH"
[[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm" # Load RVM into a shell session *as a function*

# ulimit for mongo warning added by dungnv 28/9
ulimit -n 2048

##
# Your previous /Users/dungnv/.bash_profile file was backed up as /Users/dungnv/.bash_profile.macports-saved_2014-05-22_at_10:54:28
##

# MacPorts Installer addition on 2014-05-22_at_10:54:28: adding an appropriate PATH variable for use with MacPorts.
export PATH=/opt/local/bin:/opt/local/sbin:$PATH
export PATH="$PATH:/opt/local/bin"
export PATH="$PATH:/home/blondie/.composer/vendor/bin/"
export DB_PASSWORD='root'
# Finished adapting your PATH environment variable for use with MacPorts.


HISTFILESIZE=12080
