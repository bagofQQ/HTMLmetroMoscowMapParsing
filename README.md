# HTMLmetroMoscowMapParsing
Задача - написать программу, которая будет получать HTML-код страницы «Список станций Московского метрополитена» https://www.moscowmap.ru/metro.html#lines 
с помощью библиотеки jsoup(DownloadMetroInfo в папке DownloadMetro), далее парсить полученную страницу и получать из нее линии московского метро, станции, переходы. 

Программа записывает JSON-файл со всей информацией по Московскому метрополитену (станции, линии, переходы), а также выводит в консоль 
количество станций на каждой линии и количество переходов.