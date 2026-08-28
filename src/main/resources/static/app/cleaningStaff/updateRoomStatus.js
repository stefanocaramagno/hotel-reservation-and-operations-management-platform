// Startpoint per Visualizzare la Lista dei Task di Pulizia
async function getRoomsPendingClean() {
    try {
        const response = await fetch('/api/hotelManagementSystem/getRoomsPendingClean');
        const rooms = await response.json();
    
        const mainContainer = document.querySelector('.space-y-6');
        mainContainer.innerHTML = '';
    
        rooms.forEach(function(room) {
            const roomStatusHtml = `
                <div id="task-room-${room.idRoom}" class="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition-shadow">
                    <h2 class="text-2xl font-bold text-gray-800 mb-4">Room #${room.idRoom}</h2>
                    <div class="flex items-center bg-red-100 p-4 rounded-lg mb-6">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-red-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                        </svg>
                        <p class="text-lg font-bold text-red-800">Status: Dirty</p>
                    </div>
                    <div class="flex flex-col md:flex-row md:justify-between md:items-center space-y-4 md:space-y-0">
                        <div class="text-gray-600">
                            <p><strong>Room Type:</strong> ${room.roomCategory.roomType}</p>
                            <p><strong>Capacity:</strong> ${room.roomCategory.capacity}</p>
                            <p><strong>Price for Night:</strong> ${room.roomCategory.priceForNight}</p>
                            <p><strong>Accessories:</strong> ${room.roomCategory.accessories}</p>
                        </div>
                        <div class="flex flex-col md:flex-row md:items-center md:space-x-4">
                            <label for="status-room-${room.idRoom}" class="text-gray-700 font-medium">Update Status</label>
                            <select id="status-room-${room.idRoom}" class="mt-2 md:mt-0 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:border-blue-500">
                                <option value="Clean">Clean</option>
                                <option value="Dirty" selected>Dirty</option>
                            </select>
                            <button onclick="manageCleaningTask(${room.idRoom})" class="mt-4 md:mt-0 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition">
                                Update
                            </button>
                        </div>
                    </div>
                </div>
            `;
            mainContainer.innerHTML += roomStatusHtml;
        });
    } catch (error) {
        console.error('Error loading room statuses:', error);
    }
}
  
// Startpoint per Gestire l'assegnazione di un Task di Pulizia
async function manageCleaningTask(idRoom) {
    const newStatus = document.getElementById(`status-room-${idRoom}`).value;

    const queryStringParameters = new URLSearchParams({
        idRoom: idRoom,
        newStatus: newStatus,
      }).toString();

    try {
        const response = await fetch(`/api/hotelManagementSystem/manageCleaningTask?${queryStringParameters}`, {
            method: 'POST',
        });

        if (response.ok) {
            alert('Room status updated successfully!');
            const taskElement = document.getElementById(`task-room-${idRoom}`);
            if (taskElement) {
                taskElement.remove();
            }
        } else {
            const errorMessage = await response.text();
            alert(`Error: ${errorMessage}`);
        }
    } catch (error) {
        console.error('Error updating room status:', error);
    }
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getRoomsPendingClean);
