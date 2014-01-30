# Запросы:

редактируй как нужно запросы. 

## Кол-во новых новостей:

/ttt.php?action=count=&ts=1390192108
где ts время последнего обращения

<pre>
{ "error_code" : 0, "count" : 4, "ts" : 1390897422 }
</pre>
где ts текущее время

## Список новостей:

/ttt.php?action=list&ts=1390192108
где ts время последнего обращения

<pre>
{"error_code":0,"ts":"1390897303","data":[{"id":"4","name":"News4","shorttext":"ShortNews4","date":"09.03.1982 23:59:16"}....]}
</pre>
где ts текущее время

## Одна новость:

/ttt.php?action=details&id=34234

<pre>
{"error_code":0, "images" : [1,2,3,4], "id":"4", "name":"News4", "shorttext":"ShortNews4", "text" : "FullText4", "date":"09.03.1982 23:59:16"} }
</pre>

## Картинки:

/ttt.php?action=image&id=6 - возвращает картинку в полном размере

/image.php?id=123345&w=100 - возвращает маштабированную картинку с шириной 100 пикселей

/image.php?id=123123&h=100 - возвращает маштабированную картинку с высотой 100 пикселей



Пример использования ListView, который используется в приложении.
http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
                                                                                                                                                                                                            
~                                                                                       
