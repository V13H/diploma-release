# You-can-do-it
Task manager

Технологии:Spring Boot,Spring Data JPA,Spring Security,Thymeleaf,MaterializeCSS.

Последнее как альтернатива Bootstrap(очень хотелось сделать в стиле Material Design.
В приложении можно добавлять,отмечать как выполненные,удалять активные и перезапускать проваленные задачи.

Есть сортировка и фильтрация задач.

Также есть система СМС напоминаний о задачах.Сделано посредством SQL шлюза turbosms.ua.В силу этого
настраивать приложение для работы с двумя базами данных.Сделал по туториалу.

Сообщения будут приходить с подписью Faceless.Пробовал настроить подпись с названием приложения,но система почему-то такую подпись не одобрила.
Там по тексту сообщения и так будт понятно,что это уведомление от приложения.

И ещё есть система достижений.Сложность получения намеренно занижена для того,чтобы проще было всё это опробовать.
Админка примитивная и позволяет просто просматривать зарегестрированных пользователей,их данные и количество СМС кредитов.

Админ

Логин:admin@gmail.com

Пароль:password

Админ также может добавлять задания и получать СМС уведомления.

Также есть пользователь с уже добавленными заданиями
Логин:junior@gmail.com

Пароль:password

Стыдно,конечно,что так много времени у меня ушло на написание проекта.Очень долго игрался с визуальной частью:сначала попробовал
Vaadin,потом Bootstrap,Material Bootstrap.Остановился на MaterializeCSS.Многое для себя прояснил но многое ещё предстоит.Думаю дальнешие проекты
я буду писать увереннее и быстрее.

Очень блягодарен Вам за курс.

П.С.Имели место быть проблемы с БД.Задумывалось сделать связь многие к многим достижений и пользователей.При этом количество достижений не меняется.
Всё шло хорошо пока достижения получал один пользователь.Как только те же достижения получал другой пользователь вылетала ошибка нарушения уникальности 
первичного ключа.Долго промучившись я решения не нашёл и сделал по другому.Буду благодарен за пояснения и адекватную критику.

Документация MaterializeCSS:

https://materializecss.com/
