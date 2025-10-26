// Small UI helpers for the booking app
document.addEventListener('DOMContentLoaded', ()=>{
  // animate table rows on load
  document.querySelectorAll('table tbody tr').forEach((tr,i)=>{
    tr.style.opacity = 0;
    tr.style.transform = 'translateY(8px)';
    setTimeout(()=>{
      tr.classList.add('fade-in');
      tr.style.opacity = 1;
      tr.style.transform = 'none';
    }, 60 + i*40);
  });

  // tiny tooltip fallback for badges
  document.querySelectorAll('.badge').forEach(b=>{
    b.addEventListener('mouseenter', ()=> b.style.transform = 'translateY(-2px)');
    b.addEventListener('mouseleave', ()=> b.style.transform = 'none');
  });
});
