// Startpoint per Richiedere una Modifica di una Prenotazione confermata da un Pagamento Precedente
async function requestModifyBooking(event, idBooking) {
    event.preventDefault();

    const form = event.target;
    const startDate = form.querySelector(`#modify-start-${idBooking}`).value.trim();
    const endDate = form.querySelector(`#modify-end-${idBooking}`).value.trim();

    const queryStringParameters = new URLSearchParams({
        idBooking: idBooking,
        startDate: startDate,
        endDate: endDate,
    }).toString();

    try {
        const response = await fetch(`/api/hotelManagementSystem/requestModifyBooking?${queryStringParameters}`, {
            method: 'POST',
        });

        if (response.ok) {
            alert('Modification request submitted successfully!');
            getPaidBookingsForCustomer();
        } else {
            const errorMessage = await response.text();
            alert('Error: ' + errorMessage);
        }
    } catch (error) {
        console.error('Error submitting modification request:', error);
        alert('An unexpected error occurred.');
    }
}

// Startpoint per Calcolare il Numero di Stanze Disponibili per un determinato Tipo di Stanza e un Intervallo di Date 
async function checkRoomAvailabilityForModificationRequests(roomType, startDate, endDate, elementId) {
    const queryStringParameters = new URLSearchParams({
        roomType: roomType,
        startDate: startDate,
        endDate: endDate,
    }).toString();

    try {
        const response = await fetch(`/api/hotelManagementSystem/checkRoomAvailabilityForModificationRequests?${queryStringParameters}`);
        if (response.ok) {
            const data = await response.json();
            document.getElementById(elementId).textContent = `Rooms Available: ${data.available}`;
        } else {
            document.getElementById(elementId).textContent = "Error fetching availability.";
        }
    } catch (error) {
        console.error("Error fetching room availability:", error);
        document.getElementById(elementId).textContent = "Error fetching availability.";
    }
}

// Startpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
async function getPaidBookingsForCustomer() {
    try {
        const response = await fetch('/api/hotelManagementSystem/getPaidBookingsForCustomer');
        if (response.ok) {
            const paidBookings = await response.json();
            const container = document.getElementById('booking-list');
            container.innerHTML = '';
                
            paidBookings.forEach(function (paidBooking) {
                const paidBookingsDiv = document.createElement('div');
                paidBookingsDiv.classList.add('bg-white', 'rounded-lg', 'shadow-lg', 'p-6', 'hover:shadow-xl', 'transition-shadow');

                const availabilityElementId = `availability-${paidBooking.idBooking}`;

                paidBookingsDiv.innerHTML = `
                    <h2 class="text-2xl font-bold text-gray-800 mb-4">Paid Booking #${paidBooking.idBooking}</h2>
                    <div class="flex items-center bg-green-100 p-4 rounded-lg mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                        </svg>
                        <p class="text-lg font-bold text-green-800">Status: ${paidBooking.status}</p>
                    </div>
                    
                    <p class="text-gray-600"><strong>Room Type:</strong> ${paidBooking.roomCategory.roomType}</p>
                    <p class="text-gray-600"><strong>Current Start Date:</strong> ${paidBooking.startDate}</p>
                    <p class="text-gray-600 mb-4"><strong>Current End Date:</strong> ${paidBooking.endDate}</p>

                    <form class="flex flex-col space-y-4" onsubmit="requestModifyBooking(event, ${paidBooking.idBooking})">
                        <div>
                            <label for="modify-start-${paidBooking.idBooking}" class="text-gray-700 font-medium">New Start Date:</label>
                            <input type="date" id="modify-start-${paidBooking.idBooking}" 
                                   class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:border-blue-500"
                                   onchange="checkRoomAvailabilityForModificationRequests('${paidBooking.roomCategory.roomType}', this.value, document.querySelector('#modify-end-${paidBooking.idBooking}').value, 'availability-${paidBooking.idBooking}')">
                        </div>
                        <div>
                            <label for="modify-end-${paidBooking.idBooking}" class="text-gray-700 font-medium">New End Date:</label>
                            <input type="date" id="modify-end-${paidBooking.idBooking}" 
                                   class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:border-blue-500"
                                   onchange="checkRoomAvailabilityForModificationRequests('${paidBooking.roomCategory.roomType}', document.querySelector('#modify-start-${paidBooking.idBooking}').value, this.value, 'availability-${paidBooking.idBooking}')">
                        </div>

                        <div class="flex items-center bg-green-100 p-4 rounded-lg">
                            <p class="text-lg font-bold text-green-800" id="${availabilityElementId}">Select dates to see availability.</p>
                        </div>

                        <button type="submit" class="mt-4 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition">
                            Submit Modification Request
                        </button>
                    </form>
                `;

                container.appendChild(paidBookingsDiv);
            });
        } else {
            console.error('Failed to fetch paid booking list');
        }
    } catch (error) {
        console.error('Error fetching paid booking list:', error);
    }
}

// Configurazione dei Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getPaidBookingsForCustomer);
