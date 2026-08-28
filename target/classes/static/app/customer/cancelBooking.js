// Startpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
async function getPaidBookingsForCustomer() {
    try {
        const response = await fetch('/api/hotelManagementSystem/getPaidBookingsForCustomer');
        if (response.ok) {
            const paidBookings = await response.json();
            const container = document.getElementById('booking-list');
            container.innerHTML = '';

            paidBookings
                .filter(function(paidBookings) {
                    return paidBookings.status !== 'Cancelled';
                })
                .forEach(function(paidBookings) {
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
                        <h2 class="text-2xl font-bold text-gray-800 mb-4">Paid Booking #${paidBookings.idBooking}</h2>
                        <div class="flex items-center bg-green-100 p-4 rounded-lg mb-4">
                            <p class="text-lg font-bold text-green-800">Status: ${paidBookings.status}</p>
                        </div>
                            <p><strong>Room Type:</strong> ${paidBookings.roomType}</p>
                            <p><strong>Capacity:</strong> ${paidBookings.roomCategory.capacity} Person</p>
                            <p><strong>Start Date:</strong> ${paidBookings.startDate}</p>
                            <p><strong>End Date:</strong>  ${paidBookings.endDate}</p>
                            <p><strong>Price for Night:</strong> $${paidBookings.roomCategory.priceForNight}</p>
                            <p><strong>Total Price:</strong> $${paidBookings.totalPrice}</p>
                            <p><strong>Accessories:</strong> ${paidBookings.roomCategory.accessories}</p>
                        <div class="mt-6 flex justify-end">
                        <button 
                            class="bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 transition" 
                            onclick="cancelBookingForCustomer(${paidBookings.idBooking})">
                            Cancel Booking
                        </button>
                        </div>
                    `;
                    container.appendChild(paidBookingsDiv);
                });
        } else {
            console.error('Failed to fetch paid bookings.');
        }
    } catch (error) {
        console.error('Error fetching paid bookings:', error);
    }
}

// Startpoint per Annullare una Prenotazione confermata da un Pagamento Precedente
async function cancelBookingForCustomer(idBooking) {
    const queryStringParameters = new URLSearchParams({
        idBooking: idBooking,
      }).toString();
      
    try {
        const response = await fetch(`/api/hotelManagementSystem/cancelBookingForCustomer?${queryStringParameters}`, {
            method: 'POST',
        });

        if (response.ok) {
            alert('Booking cancelled successfully!');
            getPaidBookingsForCustomer();
        } else {
            const errorMessage = await response.text();
            alert('Error: ' + errorMessage);
        }
    } catch (error) {
        console.error('Error cancelling booking:', error);
        alert('An unexpected error occurred.');
    }
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getPaidBookingsForCustomer);
