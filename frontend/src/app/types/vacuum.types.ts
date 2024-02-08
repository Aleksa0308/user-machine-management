import {User} from "./user.types";

export interface Error{
  id: number;
  action: string;
  date: Date;
  message: string;
  vacuum: Vacuum;
}

export interface Vacuum{
  id: number;
  active: boolean;
  name: string;
  creationDate: Date;
  status: string;
  createdBy: User;
}

export interface VacuumsResponse{
  vacuums: Vacuum[];
}

export interface SearchVacuums{
  name: string | null;
  status: string;
  dateFrom: Date | "";
  dateTo: Date | "";
}

export interface SearchOptions{
  code: number;
  name: string;
}
