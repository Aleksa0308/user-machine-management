export interface LoginResponse {
  jwt: string;
}
export interface decodedJwt {
  sub: string;
  permissions: string[];
  iat: number;
  exp: number;
}
export interface User{
  userId: number;
  firstname: string;
  lastname: string;
  email: string;
  permissions: Permission[];
}
export interface Permission{
  permissionId: number;
  name: string;
}
export interface UsersResponse{
  users: User[];
}

export interface UserResponse{
  user: User;
}
