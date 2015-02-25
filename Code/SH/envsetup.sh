function hmm() {
cat <<EOF
Invoke ". scripts/envsetup.sh" from your shell to add the following 
functions to your environment:
EOF
    sort $(gettop)/.hmm |awk -F @ '{printf "%-30s %s\n",$1,$2}'
}

function gettop
{
    local TOPFILE=scripts/envsetup.sh
    if [ -n "$TOP" -a -f "$TOP/$TOPFILE" ] ; then
        echo $TOP
    else
        if [ -f $TOPFILE ] ; then
            # The following circumlocution (repeated below as well) ensures
            # that we record the true directory name and not one that is
            # faked up with symlink names.
            PWD= /bin/pwd
        else
            # We redirect cd to /dev/null in case it's aliased to
            # a command that prints something as a side-effect
            # (like pushd)
            local HERE=$PWD
            T=
            while [ \( ! \( -f $TOPFILE \) \) -a \( $PWD != "/" \) ]; do
                cd .. > /dev/null
                T=`PWD= /bin/pwd`
            done
            cd $HERE > /dev/null
            if [ -f "$T/$TOPFILE" ]; then
                echo $T
            fi
        fi
    fi
}
T=$(gettop)
rm -f $T/.hmm $T/.hmmv
echo "gettop@display the top directory" >> $T/.hmm

function croot()
{
    T=$(gettop)
    if [ "$T" ]; then
        cd $(gettop)
    else
        echo "Couldn't locate the top of the tree.  Try setting TOP."
    fi
}
echo "croot@Change back to the top dir" >> $T/.hmm

function csgrep()
{
    find . -name .repo -prune -o -name .git -prune -o -type f -name "*\.cs" -print0 | xargs -0 grep --color -n "$@"
}
echo 'csgrep@Grep through all csharp code' >> $T/.hmm

sb-reset ()
{
    (
    git clean -f -d -x
    git reset --hard HEAD
    )
}
echo 'sb-reset@CAUTION: Destructively resets to clean repo' >> $T/.hmm

sb-build-vars ()
{
    UNITY=/Applications/Unity/Unity.app/Contents/MacOS/Unity
    XCODEBUILD=/usr/bin/xcodebuild
    XCRUN=/usr/bin/xcrun
    APP=sharkbomber
    APPDIR=$(gettop)/shark-bomber-ios/build
    IPADIR=$(gettop)/shark-bomber-ios/ipa
    space=" "
    PROVPRO_PATH="$HOME/Library/MobileDevice/Provisioning${SPACE} Profiles/${IOS_PROVISIONING_PROFILE_GUID}.mobileprovision"
}

sb-build-ios ()
{
    if [ -z "$IOS_SIGNING_IDENTITY" ]; then
        cat >&2 <<EOF
ERROR: IOS_SIGNING_IDENTITY not set.
It should be something like this:
export IOS_SIGNING_IDENTITY="iPhone Developer: Fred Flintstone (ABCDEF1GH2)"
or
export IOS_SIGNING_IDENTITY="iPhone Distribution: Slate Industries (ZYZZAA9FU3)"
EOF
        return 1
    fi
    if [ -z "$IOS_PROVISIONING_PROFILE_GUID" ]; then
        cat >&2 <<EOF
ERROR: IOS_PROVISIONING_PROFILE_GUID not set.
It should be something like this:
export IOS_PROVISIONING_PROFILE_GUID="DEADBEEF-FAIC-F00D-FEED-FACEB005D00F"
To find it:
1) Bring up xcode
2) Organizer
3) Provisioning Profiles
4) Find the one you want -- maybe somethig like "IOS Team Provisioning Profile: *"?
5) Right click -> Reveal in finder
6) The one you want is selected. Click on it to change its name. Command-C to copy, but don't change its name!
7) 
export IOS_PROVISIONING_PROFILE_GUID="<what you copied to clipboard goes here>"
EOF
        return 1
    fi
    (
    sb-build-vars

    echo "Step 1/3: unity build" &&
    $UNITY \
        -projectPath $(gettop)/shark-bomber \
        -executeMethod Build.iOS \
        -batchmode \
        -quit &&

    echo "Step 2/3: build .app" &&
    cd $(gettop)/shark-bomber-ios &&
    $XCODEBUILD \
        -target Unity-iPhone \
        -configuration Release \
        -sdk iphoneos \
        clean build &&

    # http://stackoverflow.com/a/5678246/9648
    echo "Step 3/3: build .ipa" &&
    rm -rf $IPADIR &&
    mkdir -p $IPADIR &&
    $XCRUN \
        -sdk iphoneos \
        PackageApplication \
            -v "${APPDIR}/${APP}.app" \
            -o "${IPADIR}/${APP}.ipa" \
            --sign "${IOS_SIGNING_IDENTITY}" \
            --embed "${PROVPRO_PATH}" &&

    echo "Complete!"
    )
}
echo 'sb-build-ios@Build shark-bomber for iOS' >> $T/.hmm

sb-ios-from-scratch ()
{
	sb-reset
	sb-build-ios
}
echo 'sb-io-from-scratch@CAUTION: Destructively build shark-bomber for iOS from scratch' >> $T/.hmm

sb-build-android ()
{
    (
    sb-build-vars

    $UNITY \
        -projectPath $(gettop)/shark-bomber    \
        -executeMethod Build.Android    \
        -batchmode \
        -quit
    )
}
echo 'sb-build-android@Build shark-bomber for Android' >> $T/.hmm

unset T f
