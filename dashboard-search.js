// City dropdown & filter logic
document.addEventListener('DOMContentLoaded', function() {
  const dropdownToggle = document.getElementById('dropdownToggle');
  const dropdownPanel = document.getElementById('dropdownPanel');
  const cityList = document.getElementById('cityList');
  const dropdownFilter = document.getElementById('dropdownFilter');
  const clearFilter = document.getElementById('clearFilter');
  const citySearchInput = document.getElementById('citySearchInput');

  // sample cities - list can be extended or fetched from API
  const cities = [
    "Agra","Ahmedabad","Bengaluru","Bhopal","Chandigarh","Chennai","Dehradun","Delhi","Guwahati",
    "Hyderabad","Jaipur","Jammu","Kolkata","Lucknow","Madurai","Mangalore","Mumbai","Nagpur",
    "Patna","Pune","Surat","Vadodara","Visakhapatnam","Varanasi","Udaipur"
  ];

  function renderList(filter='') {
    cityList.innerHTML = '';
    const q = filter.trim().toLowerCase();
    const matches = cities.filter(c => c.toLowerCase().includes(q));
    matches.forEach((c, idx) => {
      const li = document.createElement('li');
      li.textContent = c;
      li.setAttribute('role','option');
      li.addEventListener('click', () => {
        citySearchInput.value = c;
        closePanel();
        citySearchInput.dispatchEvent(new Event('input'));
      });
      cityList.appendChild(li);
    });
    if (matches.length === 0) {
      const li = document.createElement('li');
      li.textContent = 'No cities found';
      li.style.color = 'var(--muted)';
      li.style.cursor = 'default';
      cityList.appendChild(li);
    }
  }

  function openPanel() { dropdownPanel.hidden = false; renderList(dropdownFilter.value); }
  function closePanel() { dropdownPanel.hidden = true; }

  dropdownToggle.addEventListener('click', (e) => {
    e.stopPropagation();
    if (dropdownPanel.hidden) openPanel(); else closePanel();
  });

  citySearchInput.addEventListener('focus', () => { openPanel(); });

  dropdownFilter.addEventListener('input', (e) => { renderList(e.target.value); });

  clearFilter.addEventListener('click', () => { dropdownFilter.value=''; renderList(''); dropdownFilter.focus(); });

  // close when clicking outside
  document.addEventListener('click', (e) => {
    if (!document.getElementById('searchDropdown').contains(e.target)) {
      closePanel();
    }
  });

  // optional: filter main input as user types (placeholder behavior)
  citySearchInput.addEventListener('input', (e) => {
    // Here you can trigger search/filter of passengers based on input value
    // For demo, just log
    // console.log('Search:', e.target.value);
  });

  // initial render
  renderList('');
});
