# Forum
Приложение представляет собой нетематический форум для обмена сообщениями между пользователями.

_______

**Возможности**

В приложении имеется 5 основных ролей пользователей. Ниже они перечислены в порядке возрастания прав.

1) Незарегистрированный пользователь
2) Простой пользователь
3) Модератор
4) Администратор
5) Владелец

Каждая роль описывается списком прав на те или иные действия, например:

- ставить лайк/дизлайк сообщениям;
- создавать разделы;
- создавать темы;
- писать сообщения;
- редактировать чужие сообщения.

На рисунке приведены все возможные действия пользователей на форуме в соответствии с их ролями.

![image](https://github.com/pankoivan/Forum/assets/81259100/f3df5199-8b92-4a88-903b-f3e95a57597e)

Помимо действий, обозначенных на картинке, для пользователя с ролью "Владелец" предусмотрена возможность динамического добавления/удаления ролей и прав, а также перераспределения прав между разными ролями. Она использовалась на начальном этапе разработки в качестве альтернативы постоянному изменению содержимого соответствующих таблиц посредством SQL-запросов, когда роли, права и связи между ними только прорабатывались.

_______

**Особенности реализации**

Как видно, в приложении есть много действий, которые логично и естественно было бы реализовать с использованием js-кода для динамического обновления содержимого страниц. Например, лайк/дизлайк сообщениям, отправка сообщения, смена роли пользователя, бан пользователя и так далее.

Однако при разработке JavaScript не был использован. Все данные со стороны клиента отправляются на сервер через простые POST-формы. Так, например, кнопка лайка/дизлайка на сообщениях является submit-кнопкой внутри POST-формы, завязанной на определённый адрес обработки. На сервере за приём любых данных отвечают MVC-контроллеры, которые в ответ на любой запрос выдают полностью обновлённые HTML-страницы.

Поэтому данный форум интересен в качестве демонстрации того, как может выглядеть Web-приложение, предоставляющее пользователям возможность осуществления множества разных действий, но не использующее при этом в своей работе какой бы то ни было js-код.
