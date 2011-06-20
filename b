#!/bin/sh

TOMCAT=/usr/share/tomcat7
WEBAPP=${TOMCAT}/webapps
WEB=WebContent
ROOT=${WEBAPP}/ROOT
BIN=/tmp/bin

if [ ! -d $BIN ];then
    echo "[ MD ] $BIN"
    mkdir -p $BIN
fi

if [ ! -d bin ];then
    echo [ LN ] bin
    ln -s $BIN bin
fi

if [ ! -d ${WEB}/WEB-INF/lib ];then
    echo [ LN ] lib
    ln -s `pwd`/lib ${WEB}/WEB-INF/lib
fi

if [ ! -d ${WEB}/WEB-INF/classes ];then
    echo [ LN ] classes
    ln -s $BIN ${WEB}/WEB-INF/classes
fi

if [ ! -d ${ROOT} ];then
    echo [ LN ] ${ROOT}
    ln -s `pwd`/${WEB} ${ROOT}
    chown tomcat:tomcat ${ROOT}
fi

echo [ ANT ] build
ant || exit

echo [ CLR ] clean log
for i in $TOMCAT/logs/* ;do
> $i
done

echo [ TOMCAT ] restart
/etc/rc.d/tomcat7 restart
