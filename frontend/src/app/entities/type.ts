export class Type {
  id: number;
  name: string;
  icon: string;

  constructor(json: any) {
    this.id = json.id;
    this.name = json.name;
    this.icon = json.icon;
  }
}
