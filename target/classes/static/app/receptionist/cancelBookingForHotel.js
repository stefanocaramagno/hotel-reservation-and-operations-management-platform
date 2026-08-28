// Startpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
async function getPaidBookingsForReceptionist() {
    try {
        const response = await fetch('/api/hotelManagementSystem/getPaidBookingsForReceptionist');
        if (response.ok) {
            const paidBokings = await response.json();
            const container = document.getElementById('booking-list');
            container.innerHTML = '';

            paidBokings.forEach(function(paidBooking) {
                const paidBokingsDiv = document.createElement('div');
                paidBokingsDiv.classList.add('bg-white', 'rounded-lg', 'shadow-lg', 'p-6', 'hover:shadow-xl', 'transition-shadow');
                paidBokingsDiv.innerHTML = `
                    <h2 class="text-2xl font-bold text-gray-800 mb-4">Paid Booking #${paidBooking.idBooking}</h2>
                    <div class="flex items-center bg-blue-100 p-4 rounded-lg mb-4">
                      <p class="text-lg font-bold text-blue-800">Customer Name: ${paidBooking.customerName}</p>
                    </div>
                    <div class="flex items-center bg-green-100 p-4 rounded-lg mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                        </svg>
                        <p class="text-lg font-bold text-green-800">Status: ${paidBooking.status}</p>
                    </div>
                    <div class="text-gray-600 mb-4">
                      <p><strong>Room Type:</strong> ${paidBooking.roomType}</p>
                      <p><strong>Capacity:</strong> ${paidBooking.roomCategory.capacity} Person</p>
                      <p><strong>Start Date:</strong> ${paidBooking.startDate}</p>
                      <p><strong>End Date:</strong>  ${paidBooking.endDate}</p>
                      <p><strong>Price for Night:</strong> $${paidBooking.roomCategory.priceForNight}</p>
                      <p><strong>Total Price:</strong> $${paidBooking.totalPrice}</p>
                      <p><strong>Accessories:</strong> ${paidBooking.roomCategory.accessories}</p>
                    </div>
                    <div class="mt-6 flex justify-end">
                        <button 
                            class="bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 transition" 
                            onclick="cancelBookingForReceptionist(${paidBooking.idBooking})">
                            Cancel Booking
                        </button>
                    </div>
                `;
                container.appendChild(paidBokingsDiv);
            });
        } else {
            console.error('Failed to load bookings');
        }
    } catch (error) {
        console.error('Error loading bookings:', error);
    }
}

// Startpoint per Cancellare una Prenotazione confermata da un Pagamento Precedente
async function cancelBookingForReceptionist(idBooking) {
    const queryStringParameters = new URLSearchParams({
        idBooking: idBooking,
      }).toString();

    try {
        const response = await fetch(`/api/hotelManagementSystem/cancelBookingForReceptionist?${queryStringParameters}`, {
            method: 'POST',
        });

        if (response.ok) {
            alert('Booking cancelled successfully!');
            getPaidBookingsForReceptionist(); 
        } else {
            const errorMessage = await response.text();
            alert('Error cancelling booking: ' + errorMessage);
        }
    } catch (error) {
        console.error('Error cancelling booking:', error);
        alert('An unexpected error occurred.');
    }
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getPaidBookingsForReceptionist);
