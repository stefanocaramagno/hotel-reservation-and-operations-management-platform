// Startpoint per Visualizzare la Lista delle Richieste di Prenotazione
async function getRequestsBooking() {
  try {
    const response = await fetch('/api/hotelManagementSystem/getRequestsBooking');
    const bookingRequests = await response.json();
    console.log('Booking Requests from API:', bookingRequests); 

    const container = document.querySelector('#booking-requests');
    container.innerHTML = '';

    bookingRequests.forEach(function(request) {
      const requestDiv = document.createElement('div');
      requestDiv.classList.add('bg-white', 'rounded-lg', 'shadow-lg', 'p-6', 'hover:shadow-xl', 'transition-shadow', 'mb-4');

      requestDiv.innerHTML = `
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Request Booking #${request.idBooking}</h2>
        <div class="text-gray-600 mb-4">
          <div class="flex items-center bg-blue-100 p-4 rounded-lg mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5.121 17.804A5.002 5.002 0 0110 15h4a5.002 5.002 0 014.879 2.804M7 7a3 3 0 016 0m5 11H2" />
            </svg>
            <p class="text-lg font-bold text-blue-800"> ${request.customerName}</p>
          </div>
          <div class="flex items-center bg-yellow-100 p-4 rounded-lg mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-yellow-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2m0 0l2-2m-2 2l-2-2m2 2V4m0 16H4m16 0h-8" />
            </svg>
            <p class="text-lg font-bold text-yellow-800">Status: ${request.status}</p>
          </div>
          <p><strong>Room Type:</strong> ${request.roomType}</p>
          <p><strong>Start Date:</strong> ${request.startDate}</p>
          <p><strong>End Date:</strong> ${request.endDate}</p>
        </div>
        <div class="mt-6 flex justify-end space-x-4">
          <button class="bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 transition" onclick="manageRequestBooking('Approved', ${request.idBooking})">
            Approve
          </button>
          <button class="bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 transition" onclick="manageRequestBooking('Rejected', ${request.idBooking})">
            Reject
          </button>
        </div>
      `;

      container.appendChild(requestDiv);
    });
  } catch (error) {
    console.error('Error fetching booking requests:', error);
  }
}

// Startpoint per Gestire una Richiesta di Prenotazione
async function manageRequestBooking(status, idBooking) {
  const queryStringParameters = new URLSearchParams({
    status: status,
    idBooking: idBooking,
  }).toString();
  
  try {
      const response = await fetch(`/api/hotelManagementSystem/manageRequestBooking?${queryStringParameters}`, {
        method: 'POST',
      });
      
      if (response.ok) {
          alert(`Booking request ${status.toLowerCase()} successfully!`);
          getRequestsBooking(); 
      } else {
          throw new Error('Failed to update booking status');
      }
  } catch (error) {
      console.error('Error updating booking status:', error);
  }
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getRequestsBooking);
