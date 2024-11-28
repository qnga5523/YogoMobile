export interface Course {
  _id: string;
  id: number;
  name: string;
  dayOfWeek: string;
  time: string;
  capacity: number;
  duration: number;
  price: number;
  type: string;
  description?: string;
  isSynced?: boolean;
}

export interface ClassInstance {
  _id: string;
  instanceId: number;
  courseId: Course;
  date: string;
  teacher: string;
  comments?: string;
  isSynced?: boolean;
}
