' Sistema El Carmen v5 — Lanzador
' Obtiene la MAC address del equipo y abre la app desde Netlify en modo app de Chrome
' La MAC se pasa como ?hwid= para amarrar la licencia al dispositivo.

Dim shell
Set shell = CreateObject("WScript.Shell")

' ── Obtener MAC address del adaptador de red activo (WMI) ─────────────────────
Dim mac
mac = ""
On Error Resume Next
Dim oWMI, oItems, oItem
Set oWMI = GetObject("winmgmts:{impersonationLevel=impersonate}!\\.\root\cimv2")
Set oItems = oWMI.ExecQuery("SELECT MACAddress FROM Win32_NetworkAdapterConfiguration WHERE IPEnabled = True")
For Each oItem In oItems
    If oItem.MACAddress <> "" Then
        mac = Replace(oItem.MACAddress, ":", "")
        Exit For
    End If
Next
On Error GoTo 0

' ── Construir URL con MAC ─────────────────────────────────────────────────────
Dim baseUrl, url
baseUrl = "https://stalwart-panda-4f910f.netlify.app"
If mac <> "" Then
    url = baseUrl & "?hwid=" & mac
Else
    url = baseUrl
End If

' ── Abrir en Chrome modo app (sin barra de navegación) ───────────────────────
Dim chromePath
chromePath = "C:\Program Files\Google\Chrome\Application\chrome.exe"
If CreateObject("Scripting.FileSystemObject").FileExists(chromePath) Then
    shell.Run Chr(34) & chromePath & Chr(34) & " --app=" & Chr(34) & url & Chr(34) & " --window-size=1024,768", 1, False
Else
    shell.Run url, 1, False
End If
