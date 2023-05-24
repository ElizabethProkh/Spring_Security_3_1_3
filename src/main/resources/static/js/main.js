function getRoles(el){
    var res = "";
    for(var i = 0; i < el.roles.length; i++){
        res = res + " "
        res = res + el.roles[i].name.replace('ROLE_', "")
    }
    return res
}


function getUserFromForm(form){
    var user = {
        id: new FormData(form).get('id'),
        name: new FormData(form).get('name'),
        surname: new FormData(form).get('surname'),
        department: new FormData(form).get('department'),
        password: new FormData(form).get('password'),
        newRole: new FormData(form).get('newRole')
    }
    return user
}

function getUserById(id){
    var msg = $.ajax("/api/admin/users/" + id, {
        async:false,
        method: "get",
        dataType: "json"
    }).responseJSON

    var user = {
        id: msg.id,
        name: msg.name,
        surname: msg.surname,
        department: msg.department,
        password: msg.password,
        roles: msg.roles
    }
    return user
}

function loadMyData() {
    $.ajax("/api/user", {
        method: "get",
        dataType: "json",
        success: function (msg) {
            const tdody = $("#users");
            var tr = $("<tr></tr>").addClass("user").appendTo(tdody)
            $("<td></td>").text(msg.id).appendTo(tr);
            $("<td></td>").text(msg.name).appendTo(tr);
            $("<td></td>").text(msg.surname).appendTo(tr);
            $("<td></td>").text(msg.department).appendTo(tr);
            $("<td></td>").text(getRoles(msg)).appendTo(tr);
        }
    })
}

function loadUserData() {
    $.ajax("/api/user", {
        method: "get",
        dataType: "json",
        success: function (user) {
            $("#userName").text(user.name);
            $("#userRoles").text(getRoles(user));
        }
    });
}


function loadUsersAdmin() {
    fetch("/api/admin/users")
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            $("#users").html('');
            data.forEach(function (el) {
                var tbody = $('#users')
                var tr = $('<tr></tr>').appendTo(tbody)
                var td
                $('<td></td>').text(el.id).appendTo(tr)
                $('<td></td>').text(el.name).appendTo(tr)
                $('<td></td>').text(el.surname).appendTo(tr)
                $('<td></td>').text(el.department).appendTo(tr)
                $('<td></td>').text(getRoles(el)).appendTo(tr)
                td = $('<td></td>').appendTo(tr)
                $('<button></button>').text('Edit').attr('onClick', 'openAndFillModalUpdate(this)')
                    .addClass('btn btn-primary')
                    .attr('data-bs-target', '#updateModal')
                    .attr('data-bs-toggle', 'modal')
                    .val(el.id).appendTo(td)
                td = $('<td></td>').appendTo(tr)
                $('<button></button>').text('Delete').appendTo(td).attr('onClick', 'openAndFillModalDelete(this)')
                    .addClass('btn btn-danger')
                    .attr('data-bs-target', '#deleteModal')
                    .attr('data-bs-toggle', 'modal')
                    .val(el.id).appendTo(td)
            });

            loadRoles(); // Загрузка ролей после загрузки пользователей
        });
}

function loadRoles() {
    fetch("api/admin/roles")
        .then((response) => {
            return response.json();
        })
        .then((roles) => {
            populateRolesDropdown(roles);
            populateRolesDropdown2(roles);
        })
        .catch((error) => {
            console.error("Failed to load roles: " + error);
        });
}

function populateRolesDropdown(roles) {
    var selectElement = $("#roleSelect");
    selectElement.empty();
    roles.forEach(function (role) {
        var option = $("<option></option>")
            .attr("value", role.name)
            .text(role.name.replace('ROLE_', ''));
        selectElement.append(option);
    });
}

function populateRolesDropdown2(roles) {
    var selectElement = $("#roleSelect2");
    selectElement.empty();
    roles.forEach(function (role) {
        var option = $("<option></option>")
            .attr("value", role.name)
            .text(role.name.replace('ROLE_', ''));
        selectElement.append(option);
    });
}


function openAndFillModalUpdate(obj) {
    var id = obj.value
    var user = getUserById(id)
    $('#form-control_id').val(user.id)
    $('#form-control_name').val(user.name)
    $('#form-control_surname').val(user.surname)
    $('#form-control_department').val(user.department)
    $('#form-control_password').val(user.password)
    $('#roleSelect2').val(getRoles(user))
}
function openAndFillModalDelete(obj) {
    var id = obj.value
    var user = getUserById(id)
    $('#form-control_delete_id').val(user.id)
    $('#form-control_delete_name').val(user.name)
    $('#form-control_delete_surname').val(user.surname)
    $('#form-control_delete_department').val(user.department)
    $('#form-control_delete_password').val(user.password)
    $('#form-control_delete_role').val(getRoles(user))
}

addUser.onsubmit = async (e) => {
    e.preventDefault();
    var user = getUserFromForm(addUser);
    let response = await fetch('/api/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user),
    });
    let result = await response.json();
    $('.error').html('');
    if(result.status === 500) {
        $('<p></p>').text('Пользователь с данной почтой существует').appendTo($('.error'))
    } else {
        $('<p></p>').css('color', 'blue').text('Пользователь успешно добавлен').appendTo($('.error'))
    }
    console.log('Пользователь: ' + user.name + ' успешно добавлен')
    loadUsersAdmin()
};

editUser.onsubmit = async (e) => {
    e.preventDefault();
    var user = getUserFromForm(editUser);
    console.log(user)
    let response = await fetch('/api/admin/users/', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user),
    });
    $('#updateModal').modal('hide')
    loadUsersAdmin()
};

deleteUser.onsubmit = async (e) => {
    e.preventDefault();
    var user = getUserFromForm(deleteUser);
    console.log(user)
    let response = await fetch('/api/admin/users/' + user.id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user),
    });
    $('#deleteModal').modal('hide')
    loadUsersAdmin()
};