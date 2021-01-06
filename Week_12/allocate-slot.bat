title allo_slot_port_6380
@echo off
setlocal enabledelayedexpansion
for /l %%i in (0,1,5461) do (
set a=%%i
echo !a!
redis-cli.exe -p 6380 cluster addslots !a!
)
pause
