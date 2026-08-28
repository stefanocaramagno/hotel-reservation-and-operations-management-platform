document.addEventListener('DOMContentLoaded', function() {
  const headerContainer = document.getElementById('header');
  const footerContainer = document.getElementById('footer');

  // Funzione per Caricare Header e Footer nella Homepage dell'Applicazione Web
  async function loadHomeComponents() {
    try {
      const headerResponse = await fetch('../component/header.html');
      const headerHTML = await headerResponse.text();
      if (headerContainer) {
        headerContainer.innerHTML = headerHTML;
      }

      const footerResponse = await fetch('../component/footer.html');
      const footerHTML = await footerResponse.text();
      if (footerContainer) {
        footerContainer.innerHTML = footerHTML;
      }

      initializeMobileMenu();
    } catch (error) {
      console.error('Error loading components for the home page:', error);
    }
  }

  // Funzione per Caricare Header e Footer in tutte le Sottopagine dell'Applicazione Web
  async function loadSubPageComponents() {
    try {
      const headerResponse = await fetch('../../component/header.html');
      const headerHTML = await headerResponse.text();
      if (headerContainer) {
        headerContainer.innerHTML = headerHTML;
      }

      const footerResponse = await fetch('../../component/footer.html');
      const footerHTML = await footerResponse.text();
      if (footerContainer) {
        footerContainer.innerHTML = footerHTML;
      }

      initializeMobileMenu();
    } catch (error) {
      console.error('Error loading components for sub-pages:', error);
    }
  }

  // Funzione per Inizializzare il Menù della Versione Mobile dell'Applicazione Web
  function initializeMobileMenu() {
    const menuBtn = document.getElementById('mobile-menu-btn');
    const mobileMenu = document.getElementById('mobile-menu');
    if (menuBtn && mobileMenu) {
      menuBtn.addEventListener('click', function() {
        if (mobileMenu.classList.contains('hidden')) {
          mobileMenu.classList.remove('hidden');
        } else {
          mobileMenu.classList.add('hidden');
        }
      });
    }
  }

  if (window.location.pathname.includes('home.html')) {
    loadHomeComponents();
  } else {
    loadSubPageComponents();
  }
});
