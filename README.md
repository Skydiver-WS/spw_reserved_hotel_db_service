# spw_reserved_hotel_db_service
This is part app for resrerved hotel
Команды для Опеншифт в Windows 11

Способ B: Ручное добавление в PATH
Найдите путь к oc (обычно в одном из мест):

text
%USERPROFILE%\.crc\bin\oc\oc.exe
C:\Program Files\Red Hat OpenShift Local\oc.exe
Добавьте путь в переменную окружения PATH:

Нажмите Win + R → sysdm.cpl

Дополнительно → Переменные среды

В Системные переменные найдите Path

Изменить → Создать

Добавьте путь к папке с oc.exe

Нажмите OK


# Добавим путь к oc в системную переменную PATH
$ocPath = "C:\Users\isu17\.crc\cache\crc_hyperv_4.19.8_amd64"
[Environment]::SetEnvironmentVariable(
"Path",
[Environment]::GetEnvironmentVariable("Path", [EnvironmentVariableTarget]::Machine) + ";$ocPath",
[EnvironmentVariableTarget]::Machine
)

# Обновим PATH в текущей сессии
$env:Path += ";$ocPath"

# Теперь можно использовать просто oc
oc version