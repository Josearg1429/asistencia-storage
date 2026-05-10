/**
 * Sistema El Carmen v5 — Servidor local Windows
 * Sirve la app en http://localhost:8080
 * Bundled con pkg como servidor.exe (no requiere Node instalado)
 */
const http = require('http');
const fs   = require('fs');
const path = require('path');

const PORT    = 8080;
const APP_DIR = path.dirname(process.execPath); // directorio del .exe

const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.css' : 'text/css',
  '.js'  : 'application/javascript',
  '.png' : 'image/png',
  '.jpg' : 'image/jpeg',
  '.ico' : 'image/x-icon',
  '.txt' : 'text/plain',
  '.json': 'application/json',
};

const server = http.createServer((req, res) => {
  let urlPath = req.url.split('?')[0];
  if (urlPath === '/') urlPath = '/index.html';

  const filePath = path.join(APP_DIR, urlPath);

  // Seguridad: solo servir dentro del directorio de la app
  if (!filePath.startsWith(APP_DIR)) {
    res.writeHead(403); res.end('Forbidden'); return;
  }

  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('404 Not Found: ' + urlPath);
      return;
    }
    const ext  = path.extname(filePath).toLowerCase();
    const mime = MIME[ext] || 'application/octet-stream';
    res.writeHead(200, {
      'Content-Type': mime,
      'Cache-Control': 'no-cache',
    });
    res.end(data);
  });
});

server.listen(PORT, '127.0.0.1', () => {
  console.log(`El Carmen v5 corriendo en http://localhost:${PORT}`);
});

server.on('error', (e) => {
  if (e.code === 'EADDRINUSE') {
    console.error(`Puerto ${PORT} en uso. Cerrando instancia anterior...`);
    process.exit(1);
  }
});
