@echo off

"C:\Program Files (x86)\WinSCP\WinSCP.com" ^
  /log="C:\WINSCP_logs\WinSCPtest.log" /ini=nul ^
  /command ^
    "open sftp://acpt@130.211.74.42/ -hostkey=""ssh-ed25519 256 4f:17:c8:71:e0:5b:ae:45:18:a1:a5:8c:91:96:35:0f"" -privatekey=""D:\Vibhav\Putty Set up\acpt-key.ppk"" -rawsettings FSProtocol=2" ^
 "cd /home/acpt/test/"  ^
 "call ./test.ksh %1" ^
 "call ./test.ksh nextoryautomation143@gmail.com" ^
 "exit"
       

set WINSCP_RESULT=%ERRORLEVEL%
if %WINSCP_RESULT% equ 0 (
  echo Success
) else (
  echo Error
)

exit /b %WINSCP_RESULT%

exit /b 1