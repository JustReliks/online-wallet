export class Type {
  id: number;
  name: string;
  code: string;
  icon: string;

  constructor(json: any) {
    this.id = json.id;
    this.name = json.name;
    this.code = json.code;
    this.icon = json.icon;
  }
}
