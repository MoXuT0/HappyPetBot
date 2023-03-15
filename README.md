## HappyDogBot ##

Телеграмм-бот, который  отвечает на популярные вопросы людей о том, что нужно знать и уметь, чтобы забрать животное из приюта.

## Команда разработчиков: <br>
[Дмитрий Лунев](https://github.com/dvlunev)<br>
[Максим Лисицин](https://github.com/Maximfsda)<br>
[Софья Беседина](https://github.com/sofibesedina444)<br>
[Роман Леонтьев](https://github.com/LeoRoA)<br>
[Денис Фисенко](https://github.com/MoXuT0)<br>
[Тамара Золотовская](https://github.com/TamaraZolotovskaya)<br>

## Технологии в проекте ##

Backend:
- Java 17
- Maven
- Spring Boot
- Spring Web
- Spring Data
- Spring JPA
- GIT
- REST
- Swagger
- Stream API

SQL:
- PostgreSQL

### Функционал для пользователя: ###

**Этап 0. Определение запроса** 
<details>
<summary>Описание</summary>

*Это входная точка общения бота с пользователем.* 

- Бот приветствует нового пользователя, рассказывает о себе и может выдать меню на выбор, с каким запросом пришел пользователь:
    - Узнать информацию о приюте (этап 1).
    - Как взять собаку из приюта (этап 2).
    - Прислать отчет о питомце (этап 3).
    - Позвать волонтера.
- Если ни один из вариантов не подходит, то бот может позвать волонтера.
- Если пользователь уже обращался к боту ранее, то новое обращение начинается с выбора запроса, с которым пришел пользователь.
</details>

**Этап 1. Консультация с новым пользователем** 
<details>
<summary>Описание</summary>

*На данном этапе бот должен давать вводную информацию о приюте: где он находится, как и когда работает, какие правила пропуска на территорию приюта, правила нахождения внутри и общения с собаками.* 

- Бот приветствует пользователя.
- Бот может рассказать о приюте.
- Бот может выдать расписание работы приюта и адрес, схему проезда.
- Бот может выдать общие рекомендации о технике безопасности на территории приюта.
- Бот может принять и записать контактные данные для связи.
- Если бот не может ответить на вопросы клиента, то можно позвать волонтера.
</details>

**Этап 2. Консультация с потенциальным хозяином животного из приюта** 
<details>
<summary>Описание</summary>

*На данном этапе бот помогает потенциальным «усыновителям» собаки из приюта разобраться с бюрократическими (как оформить договор) и бытовыми (как подготовиться к жизни с собакой) вопросами.* 

*Основная задача — дать максимально полную информацию о том, как предстоит подготовиться человеку ко встрече с новым членом семьи.* 

- Бот приветствует пользователя.
- Бот может выдать правила знакомства с собакой до того, как можно забрать ее из приюта.
- Бот может выдать список документов, необходимых для того, чтобы взять собаку из приюта.
- Бот может выдать список рекомендаций по транспортировке животного.
- Бот может выдать список рекомендаций по обустройству дома для щенка.
- Бот может выдать список рекомендаций по обустройству дома для взрослой собаки.
- Бот может выдать список рекомендаций по обустройству дома для собаки с ограниченными возможностями (зрение, передвижение).
- Бот может выдать советы кинолога по первичному общению с собакой.
- Бот может выдать рекомендации по проверенным кинологам для дальнейшего обращения к ним.
- Бот может выдать список причин, почему могут отказать и не дать забрать собаку из приюта.
- Бот может принять и записать контактные данные для связи.
- Если бот не может ответить на вопросы клиента, то можно позвать волонтера.
</details>

**Этап 3. Ведение питомца** 
<details>
<summary>Описание</summary>

*После того, как новый усыновитель забрал собаку из приюта, он обязан в течение месяца присылать информацию о том, как животное чувствует себя на новом месте. В ежедневный отчет входит следующая информация:* 

- *Фото животного.*
- *Рацион животного.*
- *Общее самочувствие и привыкание к новому месту.*
- *Изменение в поведении: отказ от старых привычек, приобретение новых.*

*Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет. Раз в два-три дня волонтеры отсматривают все присланные отчеты. В случае, если усыновитель недолжным образом заполнял отчет, волонтер через бота может дать обратную связь в стандартной форме: «Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания собаки».* 

*В базу новых усыновителей пользователь попадает через волонтера, который его туда заносит. Задача бота — принимать на вход информацию и в случае, если пользователь не присылает информацию, напоминать об этом, а если проходит более 2 дней, то отправлять запрос волонтеру на связь с усыновителем.* 

*Как только период в 30 дней заканчивается, волонтеры принимают решение о том, остается собака у хозяина или нет. Испытательный срок может быть пройден, может быть продлен на любое количество дней, а может быть не пройден.* 

- Бот может прислать форму ежедневного отчета.
- Если пользователь прислал только фото, то бот может запросить текст.
- Если пользователь прислал только текст, то бот может запросить фото.
- Бот может выдать предупреждение о том, что отчет заполняется плохо (делает волонтер): 
«*Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания собаки».*
- Если усыновитель прошел испытательный срок, то бот поздравляет его стандартным сообщением.
- Если усыновителю было назначено дополнительное время испытательного срока, то бот сообщает ему и указывает количество дополнительных дней.
- Если усыновитель не прошел испытательный срок, то бот уведомляет его об этом и дает инструкции по дальнейшим шагам.
- Если бот не может ответить на вопросы клиента, то можно позвать волонтера.
</details>

## Демонстрация работы Telegram бота ##
