// Startopoint per Visualizzare la Lista delle Richieste di Prenotazione valide per il Pagamento
async function getPayments() {
  try {
    const response = await fetch('/api/hotelManagementSystem/getPayments');
    if (response.ok) {
      const payments = await response.json();
      const container = document.querySelector('#payment-list');
      container.innerHTML = '';

      payments.forEach(function(payment) {
        const paymentDiv = document.createElement('div');
        paymentDiv.classList.add('bg-white', 'rounded-lg', 'shadow-lg', 'p-6', 'hover:shadow-xl', 'transition-shadow', 'mb-4');

        paymentDiv.innerHTML = `
          <h2 class="text-2xl font-bold text-gray-800 mb-4">Request Booking #${payment.idBooking}</h2>
          <div class="text-gray-600 mb-4">
            <div class="flex items-center bg-green-100 p-4 rounded-lg mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
              <p class="text-lg font-bold text-green-800">Status: ${payment.status}</p>
          </div>
            <p><strong>Room Type:</strong> ${payment.roomCategory.roomType}</p>
            <p><strong>Capacity:</strong> ${payment.roomCategory.capacity} Person</p>
          </div>
          <form class="flex flex-col space-y-6" onsubmit="setPayment(event, ${payment.idBooking})">
            <div>
              <h3 class="text-xl font-bold text-gray-800 mb-4">Choose a Payment Method</h3>
              <div class="flex flex-wrap gap-4">
                <!-- Visa -->
                <label class="flex items-center justify-center w-32 h-12 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-100 focus-within:ring-2 focus-within:ring-blue-500 transition">
                  <input type="radio" name="paymentMethod-${payment.idBooking}" value="visa" class="hidden peer">
                  <span class="text-sm font-medium text-gray-700 peer-checked:text-blue-600 peer-checked:font-bold">Visa</span>
                </label>

                <!-- Mastercard -->
                <label class="flex items-center justify-center w-32 h-12 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-100 focus-within:ring-2 focus-within:ring-blue-500 transition">
                  <input type="radio" name="paymentMethod-${payment.idBooking}" value="mastercard" class="hidden peer">
                  <span class="text-sm font-medium text-gray-700 peer-checked:text-blue-600 peer-checked:font-bold">Mastercard</span>
                </label>

                <!-- Paypal -->
                <label class="flex items-center justify-center w-32 h-12 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-100 focus-within:ring-2 focus-within:ring-blue-500 transition">
                  <input type="radio" name="paymentMethod-${payment.idBooking}" value="paypal" class="hidden peer">
                  <span class="text-sm font-medium text-gray-700 peer-checked:text-blue-600 peer-checked:font-bold">Paypal</span>
                </label>

                <!-- American Express -->
                <label class="flex items-center justify-center w-32 h-12 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-100 focus-within:ring-2 focus-within:ring-blue-500 transition">
                  <input type="radio" name="paymentMethod-${payment.idBooking}" value="amex" class="hidden peer">
                  <span class="text-sm font-medium text-gray-700 peer-checked:text-blue-600 peer-checked:font-bold">Amex</span>
                </label>

                <!-- Apple Pay -->
                <label class="flex items-center justify-center w-32 h-12 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-100 focus-within:ring-2 focus-within:ring-blue-500 transition">
                  <input type="radio" name="paymentMethod-${payment.idBooking}" value="applepay" class="hidden peer">
                  <span class="text-sm font-medium text-gray-700 peer-checked:text-blue-600 peer-checked:font-bold">Apple Pay</span>
                </label>

                <!-- Google Pay -->
                <label class="flex items-center justify-center w-32 h-12 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-100 focus-within:ring-2 focus-within:ring-blue-500 transition">
                  <input type="radio" name="paymentMethod-${payment.idBooking}" value="googlepay" class="hidden peer">
                  <span class="text-sm font-medium text-gray-700 peer-checked:text-blue-600 peer-checked:font-bold">Google Pay</span>
                </label>
              </div>
            </div>
            
            <div>
              <h3 class="text-xl font-bold text-gray-800 mb-4">Card Details</h3>
              <div>
                <label for="cardNumber-${payment.idBooking}" class="block text-gray-700 font-medium mb-1">Card Number</label>
                <input type="text" id="cardNumber-${payment.idBooking}" name="cardNumber" 
                       class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:border-blue-500" 
                       placeholder="1234 5678 9012 3456">
              </div>
              <div class="flex space-x-4 mt-4">
                <div class="flex-1">
                  <label for="expiryDate-${payment.idBooking}" class="block text-gray-700 font-medium mb-1">Expiry Date</label>
                  <input type="text" id="expiryDate-${payment.idBooking}" name="expiryDate" 
                         class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:border-blue-500" 
                         placeholder="MM/YY">
                </div>
                <div class="flex-1">
                  <label for="cvv-${payment.idBooking}" class="block text-gray-700 font-medium mb-1">CVV</label>
                  <input type="text" id="cvv-${payment.idBooking}" name="cvv" 
                         class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:border-blue-500" 
                         placeholder="123">
                </div>
              </div>
              <div class="mt-4">
                <label for="cardHolder-${payment.idBooking}" class="block text-gray-700 font-medium mb-1">Cardholder Name</label>
                <input type="text" id="cardHolder-${payment.idBooking}" name="cardHolder" 
                       class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:border-blue-500" 
                       placeholder="John Doe">
              </div>
            </div>

            <button type="submit" class="w-full bg-blue-600 text-white font-semibold py-2 rounded-md hover:bg-blue-700 transition-colors">
              Pay Now
            </button>
          </form>
        `;

        container.appendChild(paymentDiv);
      });
    } else {
      console.error('Failed to fetch payment list');
    }
  } catch (error) {
    console.error('Error fetching payment list:', error);
  }
}

// Startopoint per Effettuare il Pagamento di una Richiesta di Prenotazione
async function setPayment(event, idBooking) {
  event.preventDefault();

  const form = event.target;

  const paymentMethodElement = form.querySelector(`input[name="paymentMethod-${idBooking}"]:checked`);
  const cardNumber = form.querySelector(`#cardNumber-${idBooking}`).value.trim();
  const expiryDate = form.querySelector(`#expiryDate-${idBooking}`).value.trim();
  const cvv = form.querySelector(`#cvv-${idBooking}`).value.trim();
  const cardHolder = form.querySelector(`#cardHolder-${idBooking}`).value.trim();
  const paymentMethod = paymentMethodElement.value;

  const queryStringParameters = new URLSearchParams({
      paymentMethod: paymentMethod,
      cardNumber: cardNumber,
      expiryDate: expiryDate,
      cvv: cvv,
      cardHolder: cardHolder,
      idBooking: idBooking
  }).toString();

  try {
      const response = await fetch(`/api/hotelManagementSystem/setPayment?${queryStringParameters}`)
      if (response.ok) {
          alert('Payment processed successfully!');
          getPayments();
      } else {
          const errorMessage = await response.text();
          alert('Error: ' + errorMessage);
      }
  } catch (error) {
      console.error('Error processing payment:', error);
      alert('An unexpected error occurred.');
  }
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getPayments);