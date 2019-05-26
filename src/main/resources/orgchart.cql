CREATE (_001:Employee {label: 'Ian Devling', canSign: true, level: 1, x: -229})
CREATE (_001)-[:MANAGES]->(_002:Employee {label: 'Davolio Nancy', canSign: true, level: 2, x: -500})
CREATE (_001)-[:MANAGES]->(_003:Employee {label: 'Leverling Janet', canSign: false, level: 2, x: -304})
CREATE (_001)-[:MANAGES]->(_004:Employee {label: 'Peacock Margaret', level: 2, x: -20})

CREATE (_002)-[:MANAGES]->(_005:Employee {label: 'Fuller Andrew', canSign: false, level: 3, x: -300})

CREATE (_003)-[:MANAGES]->(_006:Employee {label: 'Buchanan Steven', level: 3, x: -150})
CREATE (_003)-[:MANAGES]->(_007:Employee {label: 'Suyama Michael', canSign: false, level: 3, x: 0})
CREATE (_003)-[:MANAGES]->(_008:Employee {label: 'King Robert', canSign: false, level: 3, x: 150})

CREATE (_004)-[:MANAGES]->(_009:Employee {label: 'West Adam', canSign: false, level: 3, x: 300})
CREATE (_004)-[:MANAGES]->(_010:Employee {label: 'Charlotte Cooper', canSign: false, level: 3, x: 450})
CREATE (_004)-[:MANAGES]->(_011:Employee {label: 'Yoshi Nagase', level: 3, x: 600})

CREATE (_003)-[:MANAGES]->(_009)
