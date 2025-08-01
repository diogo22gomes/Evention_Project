import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import fs from 'fs';
import path, { dirname } from 'path';
import { fileURLToPath } from 'url';
import routes from './routes/routes.js';
import swaggerUi from 'swagger-ui-express';
import https from 'https';
import { scheduleExpireEventsJob } from './jobs/endEventsJob';

const app = express();
dotenv.config();
const PORT = process.env.PORT || 3000;

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// Ler certificados SSL/TLS
const key = fs.readFileSync(path.resolve('/usr/src/app/key.pem'));
const cert = fs.readFileSync(path.resolve('/usr/src/app/cert.pem'));

app.use(cors({
  origin: 'http://localhost:5173',
  credentials: true
}));
app.options('*', cors());

app.use('/uploads', express.static('/usr/src/app/public/uploads'));

app.use(express.json());
app.use('/api', routes);

// Swagger Docs
const swaggerDocument = JSON.parse(fs.readFileSync(path.resolve('docs/swagger.json')));
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));

// Endpoint de teste
app.get('/', (req, res) => {
  res.json('hello there');
});

//app.listen(PORT, () => {
//  console.log(`listening on port ${PORT}`);
//});

scheduleExpireEventsJob();

// Criar servidor HTTPS
https.createServer({ 
  key, 
  cert, 
  passphrase: 'benfica' // passphrase
}, app).listen(PORT, () => {
  console.log(`HTTPS server listening on port ${PORT}`);
});