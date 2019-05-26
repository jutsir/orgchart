class RestService {

  static serverError = "Server Error!";

  static header() {
    let myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json;charset=UTF-8");
    return myHeaders;
  }

  static saveEmployee(employee) {
    fetch('employees', {method: 'PUT', headers: RestService.header(), body: JSON.stringify(employee)}).then(data => {
      return data.json();
    }).then(json => {
      console.log('Saved: ' + JSON.stringify(json));
    }).catch(() => alert(RestService.serverError));
  }

  static createEmployee(employee, parentEmployeeId) {
    return fetch(`employees/${parentEmployeeId}`,
      {method: 'POST', headers: RestService.header(), body: JSON.stringify(employee)}).then(data => {
      return data.json();
    }).catch(() => alert(RestService.serverError));
  }

  static deleteEmployee(employeeId, parentEmployeeId) {
    fetch(`employees/${employeeId}/${parentEmployeeId}`, {method: 'DELETE'}).then(() => {
      console.log(`Deleted employee with id ${employeeId}`);
    }).catch(() => alert(RestService.serverError));
  }

  static searchEmployees(labelSearch, canSign) {
    let searchUrl = 'employees/search';
    if (labelSearch) {
      searchUrl = searchUrl + `?label=${labelSearch}`;
    }
    if(canSign) {
      searchUrl = searchUrl + `&canSign=true`;
    }
    return fetch(searchUrl, {method: 'GET', headers: RestService.header()}).then(data => {
      return data.json();
    }).catch(() => alert(RestService.serverError));
  }

  static searchCanSign() {
    return fetch("employees/search?canSign=true", {method: 'GET', headers: RestService.header()}).then(data => {
      return data.json();
    }).catch(() => alert(RestService.serverError));
  }

  static createRelationship(fromId, toId) {
    let relationship = {from: fromId, to: toId};
    fetch('relationships', {method: 'PUT', headers: RestService.header(), body: JSON.stringify(relationship)}).then(data => {
      return data.json();
    }).then(json => {
      console.log('Saved: ' + JSON.stringify(json));
    }).catch(() => alert(RestService.serverError));
  }

  static deleteRelationship(relationshipId) {
    fetch(`relationships/${relationshipId}`, {method: 'DELETE'}).then(() => {
      console.log(`Deleted relationship with id ${relationshipId}`);
    }).catch(() => alert(RestService.serverError));
  }

  static async getDataFromUrls(urls) {
    try {
      let data = await Promise.all(
        urls.map(
          url =>
            fetch(url).then(
              (response) => response.json()
            )));

      return (data);

    } catch (error) {
      console.log(error);

      throw (error);
    }
  }
}

dragElement(document.getElementById('employee-popUp'));
dragElement(document.getElementById('search-popUp'));

let selectedEmployee = null;
let selectedRelation = null;

let options = {
  manipulation: {
    enabled: false,
    addEdge: (employeeData, callback) => {
      if (employeeData.from === employeeData.to) {
        callback(null);
      } else {
        const isAlreadyPresent = relations.get({
          filter: (edge) => {
            return (edge.from === employeeData.from && edge.to === employeeData.to) ||
              (edge.from === employeeData.to && edge.to === employeeData.from)
          }
        }).length > 0;
        if (isAlreadyPresent) {
          callback(null);
        } else {
          const employeeFrom = employees.get(employeeData.from);
          const employeeTo = employees.get(employeeData.to);
          // change direction
          if (employeeFrom.level > employeeTo.level) {
            employeeData.from = employeeTo.id;
            employeeData.to = employeeFrom.id;
          }
          RestService.createRelationship(employeeData.from, employeeData.to);
          callback(employeeData);
        }
      }
      selectedEmployee = null;
      disableEditMode();
    },
    editEdge: (employeeData, callback) => {
      callback(employeeData);
      disableEditMode();
    }
  },
  interaction: {
    dragNodes: true
  },
  edges: {
    color: {
      color: '#D9D9D9',
      highlight: '#D9EAD3'
    },
    width: 2,
    smooth: {
      enabled: false,
      type: 'cubicBezier'
    },
    arrows: {
      to: {enabled: true, scaleFactor: 1, type: 'arrow'},
    }
  },
  nodes: {
    physics: false,
    color: {
      border: '#D9D9D9',
      background: '#D9D9D9',
      highlight: {
        border: '#D9EAD3',
        background: '#D9EAD3'
      }
    },
    shape: 'box',
    widthConstraint: {
      minimum: 100
    },
    heightConstraint: {
      minimum: 25
    },
    fixed: {
      x:false,
      y:true
    }
  },
  physics: {
    enabled: false,
    stabilization: false
  }
};

function disableLinkMode() {
  network.disableEditMode();
  document.getElementById('cancel-button').style.display = 'none';
}

// buttons
document.getElementById('cancel-button').onclick = () => {
  disableLinkMode();
};
document.getElementById('add-button').onclick = () => {
  disableLinkMode();
  addEmployee();
};
document.getElementById('link-button').onclick = () => {
  network.addEdgeMode();
  document.getElementById('cancel-button').style.display = 'block';
};
document.getElementById('search-button').onclick = () => searchPopup();
document.getElementById('can-sign-filter').onclick = () => canSignFilter();

function addEmployee() {
  employeePopup({label: 'First Last'}, 'Add Person', addEmployeeData);
}

function editEmployee() {
  employeePopup(selectedEmployee, 'Person data', updateEmployeeData);
}

// open popups
function employeePopup(data, operation, callback) {
  // filling in the popup DOM elements
  document.getElementById('operation').innerHTML = operation;
  document.getElementById('person-name').value = data.label;
  document.getElementById('can-sign').checked = data.canSign;
  document.getElementById('save-button').onclick = callback;

  document.getElementById('person-name').onkeypress = (event) => {
    if (event.code === 'Enter') {
      callback();
    }
  };

  // buttons
  document.getElementById('close-button').onclick = () => closeEmployeePopUp();
  const popup = document.getElementById('employee-popUp');
  popup.style.display = 'block';
  document.getElementById('person-name').focus();
}

function searchPopup() {
  let searchResultElement = document.getElementById('search-result');
  searchResultElement.innerHTML = '';
  document.getElementById('search').onclick = () => searchEmployeePopup();
  document.getElementById('close').onclick = () => closeSearchPopUp();
  const popup = document.getElementById('search-popUp');
  popup.style.display = 'block';

  document.getElementById('person-search').onkeypress = (event) => {
    if (event.code === 'Enter') {
      searchEmployeePopup()
    }
  };
  document.getElementById('person-search').focus();
}

function searchEmployeePopup() {
  const labelSearch = document.getElementById('person-search').value;
  const canSign = document.getElementById('can-sign-filter').checked;
  RestService.searchEmployees(labelSearch, canSign).then(persons => {
    let searchResultElement = document.getElementById('search-result');
    searchResultElement.innerHTML = '';

    for (const person of persons) {
      let searchElement = document.createElement('li');
      searchElement.onclick = () => {
        network.selectNodes([person.id]);
        network.focus(person.id);
        editEmployeeMode({nodes: [person.id]});
        document.getElementById('employee-buttons').style.display = 'block';
      };
      searchElement.appendChild(document.createTextNode(person.label));
      searchResultElement.appendChild(searchElement);
    }
  });
}

// close popups
function closeEmployeePopUp() {
  document.getElementById('save-button').onclick = null;
  document.getElementById('close-button').onclick = null;
  document.getElementById('employee-popUp').style.display = 'none';
}

function closeSearchPopUp() {
  document.getElementById('person-search').value = '';
  document.getElementById('search').onclick = null;
  document.getElementById('close').onclick = null;
  document.getElementById('person-search').onkeypress = null;
  document.getElementById('search-popUp').style.display = 'none';
}

// functions
function deleteEmployee() {
  if (confirm(`Do you want to remove person '${selectedEmployee.label}' from hierarchy?`)) {
    disableLinkMode();
    const parentEdges = relations.get({
      filter: (item) => {
        return item.to === selectedEmployee.id;
      }
    });

    const childEdges = relations.get({
      filter: (item) => {
        return item.from === selectedEmployee.id;
      }
    });

    updateRelations(parentEdges, childEdges);
  }

}

function updateRelations(parentNodeEdges, childEdges) {
  let parentNodeId = null;
  if (parentNodeEdges) {
    parentNodeId = parentNodeEdges[0].from;
    for (const edge of childEdges) {
      edge.from = parentNodeId;
      relations.update(edge);
    }

    relations.remove(parentNodeEdges.map((value) => value.id));
  }

  employees.remove(selectedEmployee.id);
  RestService.deleteEmployee(selectedEmployee.id, parentNodeId);
}

function updateEmployeeData() {
  selectedEmployee.label = document.getElementById('person-name').value;
  selectedEmployee.canSign = document.getElementById('can-sign').checked;
  employees.update(selectedEmployee);
  RestService.saveEmployee(selectedEmployee);
}

function addEmployeeData() {
  let newNode = {
    label: document.getElementById('person-name').value,
    canSign: document.getElementById('can-sign').checked,
    level: selectedEmployee.level + 1,
    x: selectedEmployee.x
  };
  RestService.createEmployee(newNode, selectedEmployee.id).then(response => {
    employees.add(response.employee);
    relations.add(response.relationship);
  });
}

function canSignFilter() {
  if (network.getSelectedNodes().length === 0) {
    selectedEmployee = null;
  }
  if (document.getElementById('can-sign-filter').checked) {
    RestService.searchCanSign().then(data => {
      employees.clear();
      employees.add(data);
      network.redraw();
    });
  } else {
    RestService.searchEmployees().then(data => {
      employees.clear();
      employees.add(data);
      network.redraw();
    });
  }
}


let employees = new vis.DataSet();
let relations = new vis.DataSet();

// create a network
let container = document.getElementById('myNetwork');
let network = null;

network = new vis.Network(container, {nodes: employees, edges: relations}, options);

RestService.getDataFromUrls(['employees', 'relationships']).then(data => {

  // fill nodes & edges
  employees.add(data[0]);
  relations.add(data[1]);

  // initialize your network!
  network = new vis.Network(container, {nodes: employees, edges: relations}, options);

  // events handlers
  network.on('selectNode', (params) => {
    editEmployeeMode(params);
  });

  network.on('dragEnd', (params) => {
    if (params.nodes.length === 1) {
      editEmployeeMode(params);

      // save node position
      const positions = network.getPositions(selectedEmployee.id);
      selectedEmployee.x = positions[selectedEmployee.id].x;
      selectedEmployee.y = positions[selectedEmployee.id].y;
      employees.update(selectedEmployee);
      RestService.saveEmployee(selectedEmployee);
    }
  });

  network.on('deselectNode', (params) => {
    selectedEmployee = null;
    if (params.edges.length === 1) {
      editRelationMode(params);
    } else {
      disableEditMode();
    }
  });

  network.on('deselectEdge', (params) => {
    if (params.edges.length === 0) {
      selectedRelation = null;
      disableEditMode();
    }
    if (params.edges.length === 1) {
      editRelationMode(params);
    }
  });

  network.on('selectEdge', (params) => {
    if (params.edges.length === 1 && selectedEmployee === null) {
      editRelationMode(params);
    }
  });

  network.on('doubleClick', (params) => {
    if (params.nodes.length === 1) {
      selectedEmployee = employees.get(params.nodes[0]);
      editEmployee();
    } else {
      disableEditMode();
    }
  });

});

function editEmployeeMode(params) {
  console.log('editEmployeeMode');
  console.log(params);
  selectedEmployee = employees.get(params.nodes[0]);
  console.log('selectedEmployee');
  console.log(selectedEmployee);
  document.getElementById('add-button').disabled = false;
  document.getElementById('link-button').disabled = false;
  document.getElementById('edit-button').onclick = () => {
    disableLinkMode();
    editEmployee();
  };
  document.getElementById('delete-button').onclick = () => {
    deleteEmployee();
  };
  document.getElementById('delete-button').disabled = selectedEmployee.level === 1;
  document.getElementById('employee-buttons').style.display = 'block';
}

function editRelationMode(params) {
  selectedRelation = relations.get(params.edges[0]);
  document.getElementById('add-button').disabled = true;
  document.getElementById('link-button').disabled = true;
  document.getElementById('edit-button').onclick = () => {
    network.editEdgeMode();
    document.getElementById('cancel-button').style.display = 'block';
  };
  document.getElementById('delete-button').onclick = () => {
    relations.remove(selectedRelation);
    RestService.deleteRelationship(selectedRelation.id);
    disableEditMode();
  };
  document.getElementById('delete-button').disabled = false;
  document.getElementById('employee-buttons').style.display = 'block';
}

function disableEditMode() {
  selectedRelation = null;
  network.disableEditMode();
  document.getElementById('link-button').disabled = false;
  document.getElementById('cancel-button').style.display = 'none';
  document.getElementById('employee-buttons').style.display = 'none';
}

// Make the DIV element draggable:
function dragElement(element) {
  let pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  if (document.getElementById(element.id + '-header')) {
    // if present, the header is where you move the DIV from:
    document.getElementById(element.id + '-header').onmousedown = dragMouseDown;
  } else {
    // otherwise, move the DIV from anywhere inside the DIV:
    element.onmousedown = dragMouseDown;
  }

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    element.style.top = (element.offsetTop - pos2) + 'px';
    element.style.left = (element.offsetLeft - pos1) + 'px';
  }

  function closeDragElement() {
    // stop moving when mouse button is released:
    document.onmouseup = null;
    document.onmousemove = null;
  }
}

// Tooltip engine
let showingTooltip;

document.onmouseover = (event) => {
  let target = event.target;

  let tooltip = target.getAttribute('data-tooltip');
  if (!tooltip) return;

  let tooltipElem = document.createElement('div');
  tooltipElem.className = 'tooltip';
  tooltipElem.innerHTML = tooltip;
  document.body.appendChild(tooltipElem);

  let coords = target.getBoundingClientRect();

  let left = coords.left + (target.offsetWidth - tooltipElem.offsetWidth) / 2;
  if (left < 0) left = 0; // do not exceed the left window border

  let top = coords.top - tooltipElem.offsetHeight - 5;
  if (top < 0) { // do not exceed the top window border
    top = coords.top + target.offsetHeight + 5;
  }

  tooltipElem.style.left = left + 'px';
  tooltipElem.style.top = top + 'px';

  showingTooltip = tooltipElem;
};

document.onmouseout = () => {

  if (showingTooltip) {
    document.body.removeChild(showingTooltip);
    showingTooltip = null;
  }
};

