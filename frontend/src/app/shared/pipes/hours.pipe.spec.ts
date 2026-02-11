import { HoursPipe } from './hours.pipe';

describe('HoursPipe', () => {
  const pipe = new HoursPipe();

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should transform integer hours', () => {
    expect(pipe.transform(5)).toBe('5h');
  });

  it('should transform zero to "0h"', () => {
    expect(pipe.transform(0)).toBe('0h');
  });

  it('should transform decimal hours', () => {
    expect(pipe.transform(3.5)).toBe('3.5h');
  });

  it('should return "-" for null', () => {
    expect(pipe.transform(null as any)).toBe('-');
  });

  it('should return "-" for undefined', () => {
    expect(pipe.transform(undefined as any)).toBe('-');
  });

  it('should transform large numbers', () => {
    expect(pipe.transform(100)).toBe('100h');
  });

  it('should transform single hour', () => {
    expect(pipe.transform(1)).toBe('1h');
  });

  it('should transform negative numbers', () => {
    expect(pipe.transform(-2)).toBe('-2h');
  });
});
