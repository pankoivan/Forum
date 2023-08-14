const list = document.getElementsByClassName("btn__like");

const notLikedStyle = "btn-primary";
const likedStyle = "btn-success";

for (let el of list) {
    el.addEventListener("click", () => {
        if (el.classList.contains(notLikedStyle)) {
            el.className = "btn " + likedStyle;
            el.getElementsByTagName("span")[1].textContent++;
        } else if (el.classList.contains(likedStyle)) {
            el.className = "btn " + notLikedStyle;
            el.getElementsByTagName("span")[1].textContent--;
        }
    });
}
