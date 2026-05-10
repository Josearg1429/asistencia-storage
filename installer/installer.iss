; Sistema El Carmen v5 — Script de instalación Inno Setup 6
; Genera: installer/Output/setup_ElCarmen_v5.exe

#define AppName      "Sistema El Carmen v5"
#define AppVersion   "5.0"
#define AppPublisher "Unidad Educativa El Carmen"
#define AppURL       "https://stalwart-panda-4f910f.netlify.app"
#define AppExeName   "launch.vbs"
#define AppId        "{{EC5-ELCARMEN-2025-JORNADA-ESCOLAR}"

[Setup]
AppId={#AppId}
AppName={#AppName}
AppVersion={#AppVersion}
AppVerName={#AppName} {#AppVersion}
AppPublisher={#AppPublisher}
AppPublisherURL={#AppURL}
AppSupportURL={#AppURL}
DefaultDirName={autopf}\El Carmen v5
DefaultGroupName={#AppName}
AllowNoIcons=no
OutputDir=Output
OutputBaseFilename=setup_ElCarmen_v5
SetupIconFile=assets\logo.ico
Compression=lzma2/ultra64
SolidCompression=yes
WizardStyle=modern
WizardImageFile=assets\wizard_banner.bmp
WizardSmallImageFile=assets\wizard_icon.bmp
UninstallDisplayIcon={app}\assets\logo.ico
PrivilegesRequired=admin
PrivilegesRequiredOverridesAllowed=dialog
ArchitecturesInstallIn64BitMode=x64compatible
MinVersion=10.0

[Languages]
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"

[Tasks]
Name: "desktopicon";    Description: "Crear acceso directo en el &Escritorio";   GroupDescription: "Accesos directos:"
Name: "startmenuicon";  Description: "Crear acceso directo en el &Menú Inicio";   GroupDescription: "Accesos directos:"

[Files]
; Servidor empaquetado (Node.js bundled)
Source: "assets\servidor.exe";  DestDir: "{app}"; Flags: ignoreversion

; Lanzador silencioso
Source: "launch.vbs";           DestDir: "{app}"; Flags: ignoreversion

; Ícono
Source: "assets\logo.ico";      DestDir: "{app}\assets"; Flags: ignoreversion

; Archivos web de la aplicación
Source: "..\index.html";        DestDir: "{app}"; Flags: ignoreversion
Source: "..\styles.css";        DestDir: "{app}"; Flags: ignoreversion
Source: "..\script.js";         DestDir: "{app}"; Flags: ignoreversion

; Librerías JavaScript locales
Source: "..\libs\*";            DestDir: "{app}\libs"; Flags: ignoreversion recursesubdirs

[Icons]
; Acceso directo en Escritorio
Name: "{autodesktop}\El Carmen v5"; Filename: "{sys}\wscript.exe"; \
  Parameters: """{app}\launch.vbs"""; \
  IconFilename: "{app}\assets\logo.ico"; \
  Tasks: desktopicon; \
  Comment: "Sistema de Asistencia El Carmen"

; Acceso directo en Menú Inicio
Name: "{group}\El Carmen v5"; Filename: "{sys}\wscript.exe"; \
  Parameters: """{app}\launch.vbs"""; \
  IconFilename: "{app}\assets\logo.ico"; \
  Tasks: startmenuicon; \
  Comment: "Sistema de Asistencia El Carmen"

; Desinstalador en Menú Inicio
Name: "{group}\Desinstalar El Carmen v5"; Filename: "{uninstallexe}"

[Run]
; Abrir la app al finalizar la instalación (opcional)
Filename: "{sys}\wscript.exe"; \
  Parameters: """{app}\launch.vbs"""; \
  Description: "Abrir El Carmen v5 ahora"; \
  Flags: nowait postinstall skipifsilent unchecked

[UninstallRun]
; Cerrar el servidor antes de desinstalar
Filename: "{sys}\taskkill.exe"; Parameters: "/F /IM servidor.exe"; Flags: runhidden

[Code]
// Verificar si el servidor ya está corriendo al instalar
function InitializeSetup(): Boolean;
begin
  Result := True;
end;
