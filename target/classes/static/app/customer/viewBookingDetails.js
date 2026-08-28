// Startpoint per la Creazione di una Richiesta di Prenotazione
function requestBooking() {
  document.querySelectorAll('form').forEach(function (form) {
    form.addEventListener('submit', async function (event) {
        event.preventDefault();
    
        const roomType = form.closest('.bg-white').querySelector('h2').textContent.trim();
        const startDate = form.querySelector('input[type="date"][id$="start"]').value;
        const endDate = form.querySelector('input[type="date"][id$="end"]').value;

        const queryStringParameters = new URLSearchParams({
            roomType: roomType,
            startDate: startDate,
            endDate: endDate,
          }).toString();
    
        try {
            const response = await fetch(`/api/hotelManagementSystem/requestBooking?${queryStringParameters}`, {
                method: 'POST',
            });
    
            if (response.ok) {
                alert('Booking request submitted successfully!');
            } else {
                const errorMessage = await response.text();
                alert('Error: ' + errorMessage);
            }
        } catch (error) {
            console.error('Error submitting booking:', error);
            alert('An unexpected error occurred.');
        }
    });
    
  });
}

// Startpoint per Calcolare il Numero di Stanza Disponbili per determinati Tipi di Stanza e Range di Date
function checkRoomAvailabilityforRequestBooking(roomType, startInputId, endInputId, resultElementId) {
  const startDateInput = document.getElementById(startInputId);
  const endDateInput = document.getElementById(endInputId);
  const resultElement = document.getElementById(resultElementId);

  async function updateAvailability() {
      const startDate = startDateInput.value;
      const endDate = endDateInput.value;

      const queryStringParameters = new URLSearchParams({
        roomType: roomType,
        startDate: startDate,
        endDate: endDate,
      }).toString();

      try {
          const response = await fetch(`/api/hotelManagementSystem/checkRoomAvailabilityforRequestBooking?${queryStringParameters}`);
          if (response.ok) {
              const data = await response.json();
              resultElement.textContent = `Rooms Available: ${data.available}`;
          } else {
              throw new Error("Error fetching availability.");
          }
      } catch (error) {
          console.error("Error fetching availability:", error);
          resultElement.textContent = "Error fetching availability.";
      }
  }

  startDateInput.addEventListener("change", updateAvailability);
  endDateInput.addEventListener("change", updateAvailability);
}

// Configurazione di Tutti i Listener al Caricamento della Pagina
document.addEventListener("DOMContentLoaded", function () {
  requestBooking();

  checkRoomAvailabilityforRequestBooking("Single Room", "single-room-start", "single-room-end", "single-rooms-available");
  checkRoomAvailabilityforRequestBooking("Double Room", "double-room-start", "double-room-end", "double-rooms-available");
  checkRoomAvailabilityforRequestBooking("Triple Room", "triple-room-start", "triple-room-end", "triple-rooms-available");
  checkRoomAvailabilityforRequestBooking("Quadruple Room", "quadruple-room-start", "quadruple-room-end", "quadruple-rooms-available");
  checkRoomAvailabilityforRequestBooking("Single Suite", "single-suite-start", "single-suite-end", "single-suites-available");
  checkRoomAvailabilityforRequestBooking("Double Suite", "double-suite-start", "double-suite-end", "double-suites-available");
  checkRoomAvailabilityforRequestBooking("Triple Suite", "triple-suite-start", "triple-suite-end", "triple-suites-available");
  checkRoomAvailabilityforRequestBooking("Quadruple Suite", "quadruple-suite-start", "quadruple-suite-end", "quadruple-suites-available");
});