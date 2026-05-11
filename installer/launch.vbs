' Sistema El Carmen v5 — Lanzador
' Abre la aplicación desde Netlify en modo app de Chrome

Dim shell
Set shell = CreateObject("WScript.Shell")

' Abrir en Chrome (o browser por defecto si Chrome no está)
Dim chromePath
chromePath = "C:\Program Files\Google\Chrome\Application\chrome.exe"
If CreateObject("Scripting.FileSystemObject").FileExists(chromePath) Then
    shell.Run Chr(34) & chromePath & Chr(34) & " --app=https://stalwart-panda-4f910f.netlify.app --window-size=1024,768", 1, False
Else
    shell.Run "https://stalwart-panda-4f910f.netlify.app", 1, False
End If
