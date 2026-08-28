// Startpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-Out
async function getPaidBookingsReadyForCheckOut() {
    try {
        const response = await fetch('/api/hotelManagementSystem/getPaidBookingsReadyForCheckOut');
        if (response.ok) {
            const paidBookings = await response.json();
            const container = document.getElementById('booking-list');
            container.innerHTML = '';

            paidBookings.forEach(paidBooking => {
                const paidBookingsDiv = document.createElement('div');
                paidBookingsDiv.classList.add(
                    'bg-white',
                    'rounded-lg',
                    'shadow-lg',
                    'p-6',
                    'hover:shadow-xl',
                    'transition-shadow'
                );

                paidBookingsDiv.innerHTML = `
                    <h2 class="text-2xl font-bold text-gray-800 mb-4">Paid Booking #${paidBooking.idBooking}</h2>
                    <div class="flex items-center bg-green-100 p-4 rounded-lg mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                        </svg>
                        <p class="text-lg font-bold text-green-800">Status: ${paidBooking.status}</p>
                    </div>
                    <p><strong>Room Type:</strong> ${paidBooking.roomType}</p>
                    <p><strong>Capacity:</strong> ${paidBooking.roomCategory.capacity} Person</p>
                    <p><strong>Start Date:</strong> ${paidBooking.startDate}</p>
                    <p><strong>End Date:</strong> ${paidBooking.endDate}</p>
                    <p><strong>Price per Night:</strong> $${paidBooking.roomCategory.priceForNight}</p>
                    <p><strong>Total Price:</strong> $${paidBooking.totalPrice}</p>
                    <p><strong>Accessories:</strong> ${paidBooking.roomCategory.accessories}</p>
                    <div class="mt-6 flex justify-end">
                        <button 
                            class="bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition"
                            onclick="confirmCheckOut(${paidBooking.idBooking})">
                            Check-Out Now
                        </button>
                    </div>
                `;
                container.appendChild(paidBookingsDiv);
            });
        } else {
            console.error('Failed to fetch bookings ready for check-out.');
        }
    } catch (error) {
        console.error('Error fetching bookings ready for check-out:', error);
    }
}

// Startpoint per effettuare il Check-Out di una Prenotazione confermata da un Pagamento Precedente
async function confirmCheckOut(idBooking) {
    const queryStringParameters = new URLSearchParams({
        idBooking: idBooking,
      }).toString();

    try {
        const response = await fetch(`/api/hotelManagementSystem/confirmCheckOut?${queryStringParameters}`, {
            method: 'POST',
        });

        if (response.ok) {
            alert('Check-out confirmed successfully!');
            getPaidBookingsReadyForCheckOut();
        } else {
            const errorMessage = await response.text();
            alert('Error: ' + errorMessage);
        }
    } catch (error) {
        console.error('Error confirming check-out:', error);
        alert('An unexpected error occurred.');
    }
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getPaidBookingsReadyForCheckOut);
