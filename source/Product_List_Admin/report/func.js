document.addEventListener("DOMContentLoaded", function () {
    //Đặt giá trị mặc định
    const today = new Date();
    document.getElementById("fromDate").value = formatDate(today);
    document.getElementById("toDate").value = formatDate(today);
})

function formatDate(date) {
    return date.toISOString().split("T")[0];
}

function rangeChange(value) {
    const today = new Date();
    let from, to;

    switch (value) {
      case "today":
        from = to = today;
        break;
      case "yesterday":
        from = to = new Date(today.setDate(today.getDate() - 1));
        break;
      case "week":
        from = new Date();
        from.setDate(today.getDate() - 6);
        to = new Date();
        break;
      case "thismonth":
        from = new Date(today.getFullYear(), today.getMonth(), 1);
        to = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        break;
      default:
        return;
    }

    document.getElementById("fromDate").value = formatDate(from);
    document.getElementById("toDate").value = formatDate(to);
}

