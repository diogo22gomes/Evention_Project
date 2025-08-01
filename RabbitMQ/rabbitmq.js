const amqp = require('amqplib');

// Configuração do RabbitMQ
const RABBITMQ_URL = 'amqp://admin:admin@rabbitmq:5672';
const QUEUE_NAME = 'event_notifications'; 

async function sendNotification(userId, message) {
  try {
    const connection = await amqp.connect(RABBITMQ_URL);
    const channel = await connection.createChannel();

    await channel.assertQueue(QUEUE_NAME, { durable: true });

    const notification = JSON.stringify({
      userId,
      message,
      timestamp: new Date().toISOString(),
    });

    channel.sendToQueue(QUEUE_NAME, Buffer.from(notification), {
      persistent: true, 
    });

    console.log('Mensagem enviada para a fila:', notification);

    // Fecha a conexão
    setTimeout(() => {
      channel.close();
      connection.close();
    }, 500);
  } catch (error) {
    console.error('Erro ao enviar notificação via RabbitMQ:', error);
  }
}

// Função para consumir mensagens da fila (caso queira processar as notificações)
async function consumeNotifications() {
  try {
    const connection = await amqp.connect(RABBITMQ_URL);
    const channel = await connection.createChannel();

    await channel.assertQueue(QUEUE_NAME, { durable: true });

    console.log('📢 Aguardando notificações...');

    channel.consume(QUEUE_NAME, (msg) => {
      if (msg !== null) {
        const notification = JSON.parse(msg.content.toString());
        console.log('📩 Notificação recebida:', notification);

 
        channel.ack(msg);
      }
    });
  } catch (error) {
    console.error('Erro ao consumir mensagens do RabbitMQ:', error);
  }
}

// Exporta as funções
module.exports = {
  sendNotification,
  consumeNotifications,
};
