// Startpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
async function getPaidBookingsForCustomer() {
    try {
        const response = await fetch('/api/hotelManagementSystem/getPaidBookingsForCustomer');
        if (!response.ok) {
            throw new Error('Failed to fetch accepted bookings');
        }
        const paidBookings = await response.json();

        const container = document.getElementById('accepted-bookings');
        container.innerHTML = '';

        if (paidBookings.length === 0) {
            const noData = document.createElement('p');
            noData.classList.add('text-gray-600');
            noData.textContent = 'No accepted bookings found.';
            container.appendChild(noData);
            return;
        }

        paidBookings.forEach(function (paidBookings) {
            var paidBookingsDiv = document.createElement('div');
            paidBookingsDiv.classList.add('bg-white', 'rounded-lg', 'shadow-lg', 'p-6', 'hover:shadow-xl', 'transition-shadow');

            paidBookingsDiv.innerHTML = `
              <h2 class="text-2xl font-bold text-gray-800 mb-4">Paid Booking #${paidBookings.idBooking}</h2>
              <div class="flex items-center bg-green-100 p-4 rounded-lg mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                <p class="text-lg font-bold text-green-800">Status: ${paidBookings.status}</p>
              </div>
              <div class="text-gray-600 mb-4">
                <p><strong>Room Type:</strong> ${paidBookings.roomType}</p>
                <p><strong>Capacity:</strong> ${paidBookings.roomCategory.capacity}</p>
                <p><strong>Start Date:</strong> ${paidBookings.startDate}</p>
                <p><strong>End Date:</strong>  ${paidBookings.endDate}</p>
                <p><strong>Price for Night:</strong> $${paidBookings.roomCategory.priceForNight}</p>
                <p><strong>Total Price:</strong> $${paidBookings.totalPrice}</p>
                <p><strong>Accessories:</strong> ${paidBookings.roomCategory.accessories}</p>
              </div>
            `;

            container.appendChild(paidBookingsDiv);
        });
    } catch (error) {
        console.error('Error loading accepted bookings:', error);
    }
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getPaidBookingsForCustomer);
