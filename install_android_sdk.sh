#!/bin/bash
# Installer Android SDK shell scrpit
# install git.
#apt-get update
#apt-get install -y git-core
#echo `git --version`
#
# install the Android Sdk.
# download.
# see https://developer.android.com/studio/releases/sdk-tools.html
# see https://androidsdkoffline.blogspot.pt/p/android-sdk-tools.html
# apt-get install -y default-jdk unzip expect
echo `pwd`
android_sdk_zip=tools_r25.2.3-linux.zip
cd /opt
wget -v -4 "https://dl.google.com/android/repository/${android_sdk_zip}"
cd /opt
unzip -q $android_sdk_zip -d /opt/android-sdk

# setup profile environment variables.
cat >/etc/profile.d/android-sdk.sh <<'EOF'
export ANDROID_HOME='/opt/android-sdk'
export PATH="$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools"
EOF
. /etc/profile.d/android-sdk.sh

# install packages.
# NB list available packages with android list sdk --all --extended
android_packages=(
    tools
    platform-tools
    build-tools-25.0.2
    #android-24
    #sys-img-armeabi-v7a-android-24
    #sys-img-x86-android-24 # NB thid needs kvm support. they won't work inside a VirtualBox VM.
    #sys-img-x86_64-android-24
    android-25
    #android-25 emulators does not seem to work at all...
    #sys-img-x86-google_apis-25
    #sys-img-x86_64-google_apis-25
    extra-android-m2repository
    extra-google-m2repository
)
expect <<EOF
set timeout -1;
spawn android update sdk --all --filter $(IFS=,;echo "${android_packages[*]}") --no-ui;
expect {
    "Do you accept the license" { exp_send "y\r" ; exp_continue }
    eof
}
EOF