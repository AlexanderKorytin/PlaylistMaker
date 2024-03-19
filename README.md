# Проект PlayList Maker

##### приложение для создания и редактирования плейлистов треков с ислользованием API iTunes, со встроеным медиа плеером.
-----

#### для связи telegram: @koritin84

-----

#### совместимость: Android 10 (min SDK 29)

-----

#### Версия языка kotlin.android' version '1.8.0''
Gradle JDK version '17.0.9'
#### Зависимости: Room, coroutines, viewpager2, fragment, glide, gson, retrofit2, peko

-----
### Технологии:
Git,  XML,  JSON,  Kotlin,  Room,  MVVM,  Android SDK,  Single Activity,  Fragments,  Retrofit2,  ConstraintLayout,  RecyclerView,  SharedPreferences,  Permissions, Gradle, Coroutines, Flow, LiveData, Jetpack Navigation Component, Koin, Glide, view pager 

-----
### Инструкция по установке

1. Откройте Android studio
2. Нажмите кнопку "Get from VCS"
3. В поле "URL:" вставте ссылку [github](https://github.com/AlexanderKorytin/PlaylistMaker.git) на этот проект и нажмите "clone"
4. при необходимости скачайте нужную версию Gradle JDK

### Инструкция по использованию

1 При запуске приложения откроется экран:

![Screenshot_20240319-143023](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/c01aebcb-31b3-418e-a62d-5533d07aa72b)

На вкладе представлен список избранных треков или сообщение о пустом списке. При нажатии на любой трек откроется экран медиа плеера (см п. 4)


2 Нажмите на вкладку Плейлисты - откроется экран со списком плейлистов или сообщение о том, что список пуст. При нажатии на плейлист откроется экран плейлиста (см п. 6)

![Screenshot_20240319-143027](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/ea9b14f3-78c6-4b31-a9c3-53ea5bf4fd40)

3 Нажатие на вкладку "Настойки" откроется экран настроек:

![Screenshot_20240319-143040](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/d5ace10c-80f4-43ad-bae2-d7460b853534)

4 Экран медиаплеера: 

![Screenshot_20240319-142948](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/1fdc9b5f-2714-4905-8596-a3ba55778782)

здесь можно прослушать трек, добавить/убрать трек из избранных, при нажатии на кнопку добавить в лейлист откроется нижнее меню (изображение ниже) со писком ранее созданных плейлистов (если есть) и кнопкой создания нового плейлиста при нажатии на которую откроется соответсвующий экран (см п. 5)

![Screenshot_20240319-142959](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/d0b491f7-bc9f-4aac-8c06-158cf1660d8b)

5 Экран создания плейлиста:

![Screenshot_20240319-143010](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/e1133306-af67-46a2-856f-aba66fbaed29)

Здесь можно указать название плейлиста, описание, выбрать изображение для его обложки.


6 Экран плейлиста: 

![Screenshot_20240319-145355](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/37f781b0-702f-4b25-8cf0-c56ad8bf97cc)

Здесь можно перейти на экран медиаплеера (см п. 4) по нажатию на трек, поделиться плейлистом. При нажатии на кнопку настройки откроется меню (см изображение ниже) где можно удалить плейлист, поделиться плейлистом и редактировать плейлист (будет осуществлен переход на экран аналогичный п. 5)

![Screenshot_20240319-145548](https://github.com/AlexanderKorytin/PlaylistMaker/assets/124441554/0df6a433-dced-467f-a03a-38b13f57afb6)



