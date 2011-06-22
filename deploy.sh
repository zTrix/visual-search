
TMP=/tmp/deploy.tmp
WEB=WebContent

if [ -d ${TMP} ];then
    rm -r ${TMP}
fi

echo " [ MD ] ${TMP}"
mkdir -p ${TMP}

echo " [ CP ] conf"
cp -R conf ${TMP}/

echo " [ CP ] WebContent"
cp -R ${WEB}/* ${TMP}/

for i in classes lib;do
    target=${TMP}/WEB-INF/$i
    echo " [ CP ] $target"
    if [ -h $target ];then
        dest=`readlink ${target}`
        rm $target
        mkdir -p $target
        cp -R $dest/* $target
    fi
done

cd $TMP
echo " [ ZIP ] VisualSearch.zip"
7z a -tzip VisualSearch.zip .
