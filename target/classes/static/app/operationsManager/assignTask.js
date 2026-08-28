// Startpoint per Assegnare un Task di Pulizia
async function assignCleaningTask() {
  const cleaningStaff = document.getElementById('staffMember').value;
  const dirtyRoom = document.getElementById('dirtyRoom').value;

  if (!cleaningStaff || !dirtyRoom) {
    alert('Please select both a staff member and a dirty room.');
    return;
  }

  const queryStringParameters = new URLSearchParams({
    idCleaningStaff: cleaningStaff,
    idRoom: dirtyRoom,
  }).toString();

  try {
    const response = await fetch(`/api/hotelManagementSystem/assignCleaningTask?${queryStringParameters}`, {
      method: 'POST',
    });

    if (response.ok) {
      alert('Task assigned successfully!');
      document.querySelector('form').reset();
      getDirtyRooms(); 
    } else {
      const errorMessage = await response.text();
      alert(`Error: ${errorMessage}`);
    }
  } catch (error) {
    console.error('Error assigning task:', error);
    alert('An error occurred while assigning the task.');
  }
}

// Startpoint per Visualizzare la Lista dei Cleaning Staff
async function getCleaningStaff() {
  try {
    const response = await fetch('/api/hotelManagementSystem/getAllCleaningStaff');
    if (!response.ok) throw new Error('Failed to load staff members.');

    const staffMembers = await response.json();
    const staffDropdown = document.getElementById('staffMember');
    staffDropdown.innerHTML = '<option value="">Select a staff member</option>';

    staffMembers.forEach(function(member) {
      const option = document.createElement('option');
      option.value = member.idCleaningStaff;
      option.textContent = member.fullName;
      staffDropdown.appendChild(option);
    });
  } catch (error) {
    console.error('Error loading staff members:', error);
    alert('Failed to load staff members.');
  }
}

// Startpoint per Visualizzare la Lista delle Stanze Sporche
async function getDirtyRooms() {
  try {
    const response = await fetch('/api/hotelManagementSystem/getDirtyRooms');
    if (!response.ok) throw new Error('Failed to load dirty rooms.');

    const dirtyRooms = await response.json();
    const roomDropdown = document.getElementById('dirtyRoom');
    roomDropdown.innerHTML = '<option value="">Select a dirty room</option>';

    dirtyRooms.forEach(function(room) {
      const option = document.createElement('option');
      option.value = room.idRoom;
      option.textContent = `Room #${room.idRoom}`;
      roomDropdown.appendChild(option);
    });
  } catch (error) {
    console.error('Error loading dirty rooms:', error);
    alert('Failed to load dirty rooms.');
  }
}

// Event Listener per il Form di Assegnazione
document.querySelector('form').addEventListener('submit', function(e) {
  e.preventDefault();
  assignCleaningTask();
});

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener('DOMContentLoaded', getCleaningStaff);
document.addEventListener('DOMContentLoaded', getDirtyRooms);
