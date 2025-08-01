const { sendNotification, consumeNotifications } = require('./rabbitmq');

// Teste de envio de notificação
async function test() {
  console.log('Enviando notificação...');
  await sendNotification('12345', 'Teste de notificação via RabbitMQ');

  // Iniciar o consumidor para verificar se a mensagem foi recebida
  setTimeout(() => {
    console.log('Iniciando consumo...');
    consumeNotifications();
  }, 1000);
}

test();
