@echo off
set inputFilename="access.log"
set outputFilename="result.json"
set dateFrom=""
set dateTo=""
chcp 65001
set /p inputFilename= Введите местоположение входного файла: 
set /p outputFilename= Введите местоположение выходного файла: 
set /p dateFrom= Введите начало временного периода (формат dd.MM.YYYY-HH.mm.ss): 
set /p dateTo= Введите конец временного периода (формат dd.MM.YYYY-HH.mm.ss или HH.mm.ss, если задан предыдущий аргумент): 

java -jar test.jar %inputFilename% %outputFilename% %dateFrom% %dateTo%
pause