' Sistema El Carmen v5 — Lanzador silencioso
' Inicia el servidor local y abre Chrome en localhost:8080
' Corre sin ventana de terminal visible

Dim appDir, shell
appDir = CreateObject("Scripting.FileSystemObject").GetParentFolderName(WScript.ScriptFullName)
Set shell = CreateObject("WScript.Shell")

' Iniciar servidor (oculto)
shell.Run Chr(34) & appDir & "\servidor.exe" & Chr(34), 0, False

' Esperar 1.5 segundos a que levante
WScript.Sleep 1500

' Abrir en Chrome (o browser por defecto si Chrome no está)
Dim chromePath
chromePath = "C:\Program Files\Google\Chrome\Application\chrome.exe"
If CreateObject("Scripting.FileSystemObject").FileExists(chromePath) Then
    shell.Run Chr(34) & chromePath & Chr(34) & " --app=http://localhost:8080 --window-size=1024,768", 1, False
Else
    shell.Run "http://localhost:8080", 1, False
End If
